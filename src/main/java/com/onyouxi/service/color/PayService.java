package com.onyouxi.service.color;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.onyouxi.constant.Const;
import com.onyouxi.model.dbModel.OrderModel;
import com.onyouxi.model.dbModel.TransactionModel;
import com.onyouxi.model.dbModel.UserModel;
import com.onyouxi.model.dbModel.UserOrderModel;
import com.onyouxi.service.OrderService;
import com.onyouxi.service.UserOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
public class PayService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ios.certificate.url}")
    private String certificateUrl;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserOrderService userOrderService;

    /**
     * 苹果支付验证
     * @param url
     * @param
     * @return
     */
    public JSONObject verifyReceipt(String url, String receipt, Integer times){
        if(url.isEmpty()){
            return null;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("receipt-data",receipt);
            jsonObject.put("password","a4cc1d8e9f4c4da4bd083ed6808384dd");
            HttpEntity<JSONObject> formEntity = new HttpEntity<>(jsonObject, headers);
            ResponseEntity<JSONObject> response = restTemplate.postForEntity(url,formEntity,JSONObject.class);
            JSONObject result = response.getBody();
            log.info("verifyReceipt result :{}",result.toJSONString());
            if (times < 3 && result != null) {
                if(result.getInteger("status") == 21007) {
                    return verifyReceipt("https://sandbox.itunes.apple.com/verifyReceipt", receipt, times + 1);
                }else{
                    return verifyReceipt(url, receipt, times + 1);
                }
            }
            return result;

        } catch (Exception e) {
            log.info("verifyReceipt error :{}", e);
            return verifyReceipt(url, receipt, times + 1);
        }
    }

    public Long applePay( String sign,String receipt){
        JSONObject result = verifyReceipt(certificateUrl,receipt,0);
        Integer status = result.getInteger("status");
        if(status.intValue() != 0){
            return 1L;
        }
        JSONObject receiptObject = result.getJSONObject("receipt");
        JSONArray latestReceiptInfo = result.getJSONArray("latest_receipt_info");
        String transactionId;
        String sku;
        String originalTransactionId="";
        String latestReceipt = result.getString("latest_receipt");
        List<TransactionModel> transactionModelList = new ArrayList<>();
        if(null != latestReceiptInfo && !latestReceiptInfo.isEmpty()){
            for(int i=0;i<latestReceiptInfo.size();i++){
                JSONObject trans = latestReceiptInfo.getJSONObject(i);
                if(null != trans){
                    transactionId = trans.getString("transaction_id");
                    sku = trans.getString("product_id");
                    TransactionModel transactionModel = new TransactionModel();
                    transactionModel.setProductId(sku);
                    transactionModel.setTransactionId(transactionId);
                    transactionModel.setOriginalTransactionId(trans.getString("original_transaction_id"));
                    originalTransactionId = trans.getString("original_transaction_id");
                    transactionModel.setExpiresDatMs(trans.getLong("expires_date_ms"));
                    transactionModelList.add(transactionModel);
                }
            }
        } else {
            transactionId = receiptObject.getString("transaction_id");
            sku = receiptObject.getString("product_id");
            TransactionModel transactionModel = new TransactionModel();
            transactionModel.setProductId(sku);
            transactionModel.setTransactionId(transactionId);
            transactionModel.setOriginalTransactionId(receiptObject.getString("original_transaction_id"));
            originalTransactionId=receiptObject.getString("original_transaction_id");
            transactionModel.setExpiresDatMs(receiptObject.getLong("expires_date_ms"));
            transactionModelList.add(transactionModel);
        }

        List<Long> expiresDatMsList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(transactionModelList)){
            transactionModelList.forEach(transactionModel -> {
                OrderModel orderModel = orderService.findByTransactionId(transactionModel.getTransactionId());
                //如果发现有没有成功的订单
                if( null == orderModel){
                    orderModel = new OrderModel();
                    orderModel.setSign(sign);
                    orderModel.setTransactionId(transactionModel.getTransactionId());
                    orderModel.setStatus( Const.STATUS_NORMAL);
                    orderModel.setProductId(transactionModel.getProductId());
                    orderModel.setOriginalTransactionId(transactionModel.getOriginalTransactionId());
                    orderModel.setReceipt(receipt);
                    orderModel.setType(1);
                    expiresDatMsList.add(transactionModel.getExpiresDatMs());
                    orderService.save(orderModel);
                }

            });
            UserModel userModel = userService.findByOriginalTransactionId(originalTransactionId);
            if( null == userModel){
                userModel = userService.getUserBySign(sign);
                if( null == userModel) {
                    userModel = new UserModel();
                    userModel.setSign(sign);
                    userModel.setCreateTime(System.currentTimeMillis());
                    userModel.setStatus(Const.STATUS_NORMAL);
                }
                userModel.setOriginalTransactionId(originalTransactionId);
                userModel.setLatestReceipt(latestReceipt);
            }

            if( null != userModel){
                if(!CollectionUtils.isEmpty(expiresDatMsList)) {
                    expiresDatMsList.sort(Comparator.reverseOrder());
                    Long expire = expiresDatMsList.get(0);
                    userModel.setPayExpires(expire);
                }
                userService.save(userModel);
                log.info("save user success: {}", JSON.toJSONString(userModel));
                return userModel.getPayExpires();
            }
        }

        return 1L;
    }


    public void checkApplePay(UserModel userModel){
        JSONObject result = verifyReceipt(certificateUrl,userModel.getLatestReceipt(),0);
        Integer status = result.getInteger("status");
        if(status.intValue() != 0){
            userModel.setPayStatus(3);
            userService.save(userModel);
        }
        JSONArray latestReceiptInfo = result.getJSONArray("latest_receipt_info");
        JSONArray pendingRenewalInfoList = result.getJSONArray("pending_renewal_info");
        List<TransactionModel> transactionModelList = new ArrayList<>();
        List<Long> expiresDatMsList = new ArrayList<>();
        if( null != latestReceiptInfo && !latestReceiptInfo.isEmpty()){
            for(int i=0;i<latestReceiptInfo.size();i++){
                JSONObject trans = latestReceiptInfo.getJSONObject(i);
                if(null != trans){
                    String transactionId = trans.getString("transaction_id");
                    String productId = trans.getString("product_id");
                    TransactionModel transactionModel = new TransactionModel();
                    transactionModel.setProductId(productId);
                    transactionModel.setTransactionId(transactionId);
                    transactionModel.setOriginalTransactionId(trans.getString("original_transaction_id"));
                    transactionModel.setExpiresDatMs(trans.getLong("expires_date_ms"));
                    transactionModelList.add(transactionModel);
                }
            }

            if(!CollectionUtils.isEmpty(transactionModelList)){
                transactionModelList.forEach(transactionModel -> {
                    OrderModel orderModel = orderService.findByTransactionId(transactionModel.getTransactionId());
                    //如果发现有没有成功的订单
                    if( null == orderModel){
                        orderModel = new OrderModel();
                        orderModel.setSign(userModel.getSign());
                        orderModel.setTransactionId(transactionModel.getTransactionId());
                        orderModel.setStatus( Const.STATUS_NORMAL);
                        orderModel.setProductId(transactionModel.getProductId());
                        orderModel.setOriginalTransactionId(transactionModel.getOriginalTransactionId());
                        orderModel.setType(2);
                        expiresDatMsList.add(transactionModel.getExpiresDatMs());
                        orderService.save(orderModel);
                    }

                });

            }

            if(!CollectionUtils.isEmpty(expiresDatMsList)) {
                expiresDatMsList.sort(Comparator.reverseOrder());
                Long expire = expiresDatMsList.get(0);
                //如果自动订阅的过期时间大于之前的 那么更新
                if( expire.longValue() > userModel.getPayExpires().longValue()) {
                    userModel.setPayExpires(expire);
                    userModel.setPayStatus(1);
                    userService.save(userModel);

                }else{
                    if( null != pendingRenewalInfoList && !pendingRenewalInfoList.isEmpty()){
                        for( int i=0;i<pendingRenewalInfoList.size();i++){
                            JSONObject pendingRenewalInfo =  pendingRenewalInfoList.getJSONObject(i);
                            log.info("pendingRenewalInfo :{}",pendingRenewalInfo.toJSONString());
                            Integer renewStatus =  pendingRenewalInfo.getInteger("auto_renew_status");
                            Integer period = pendingRenewalInfo.getInteger("is_in_billing_retry_period");
                            if( renewStatus == 0){
                                userModel.setPayStatus(2);
                                userService.save(userModel);
                            }else{
                                if( renewStatus == 1 && period == 1){
                                    userModel.setPayStatus(3);
                                    userService.save(userModel);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public static void main(String[] args){
        List<Long> test = new ArrayList<>();
        test.add(123L);
        test.add(456L);
        test.add(789L);
        test.add(233L);
        test.add(566L);

        test.sort(Comparator.reverseOrder());

        for(int i=0;i<test.size();i++){
            System.out.println(test.get(i));
        }
    }


}
