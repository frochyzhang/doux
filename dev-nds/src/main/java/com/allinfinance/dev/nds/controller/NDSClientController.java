package com.allinfinance.dev.nds.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.allinfinance.dev.nds.resp.NDSResponse;
import com.nds.nudetect.Date;
import com.nds.nudetect.UUID;
import com.nds.nudetect.*;
import com.nds.nudetect.threeds1.ProcessResultRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.Error;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.*;

/**
 * Classname  com.allinfinance.dev.nds.controller.NDSClientController
 *
 * @Description TODO
 * @Date 2021/4/2 15:39
 * @Created by ZhangYong
 */
@Slf4j
@RestController
public class NDSClientController {

    @Value(("${com.nds.nudetectEndPoint}"))
    private String nudetectEndPoint;
    @Value("${com.nds}")
    private static Map inputParams;

    @GetMapping("/3DSClient")
    public NDSResponse threeDSClient(HttpServletRequest request, HttpServletResponse response) {
        String sessionData = request.getParameter("sessionData");
        Base64.Decoder decoder = Base64.getDecoder();
        inputParams = JSON.parseObject(decoder.decode(sessionData), Map.class, new Feature[0]);
        log.debug("queryString:{}", request.getQueryString());
        log.debug("inputParameters:{}", inputParams.toString());

        String client3DSEndpoint = inputParams.get("clientEndPoint").toString();
        String clientId = inputParams.get("clientId").toString();
        String clientKey = inputParams.get("clientKey").toString();

        NDSClient ndsClient = null;
        final Date purchaseDate = new Date(Calendar.getInstance());

        try {
            ndsClient = NDSClient.init(clientKey);
        } catch (PublisherExceptions.ClientKeyNotSet clientKeyNotSet) {
            log.error("初始化NDSClient失败!", clientKeyNotSet);
            return NDSResponse.failure();
        }

        try {
            ndsClient.setCryptographyConfig(EncryptionKeyProvider.withEnvironment("nd-mtf"));
        } catch (IOException e) {
            log.error("加密规则配置失败!", e);
            return NDSResponse.failure();
        }

        ndsClient.setNuDetectApiBaseUrl(nudetectEndPoint);
        EnvironmentData ed = new EnvironmentData();
        ed.setSessionId(inputParams.get("sessionId").toString());
        log.debug("----------------------transaction parameters---------------------");

        TransactionParameters tp = null;

        try {
            tp = new TransactionParameters(ed);
        } catch (PublisherExceptions.InvalidEnvironment invalidEnvironment) {
            log.error("初始化交易参数失败!", invalidEnvironment);
            return NDSResponse.failure();
        }

        tp.setNotificationURL(client3DSEndpoint);
        tp.setThreeDSCallbacks(new ThreeDSCallbacks() {
            {
                this.supportedVersions.before = (tp) -> {
                    CreditCard creditCard = new CreditCard();
                    creditCard.setNumber(inputParams.get("CardNo").toString());
                    tp.addData(creditCard);
                };
                this.supportedVersions.after = (svRes) -> {
                };
                this.authenticate.before = (tp) -> {
                    AccountInfoData accountInfo = new AccountInfoData();
                    accountInfo.setAccountType(Constants.AccountType.NotApplicable);
                    accountInfo.setEmailAddress(inputParams.get("CHEmail").toString());
                    accountInfo.setPhone(inputParams.get("CHPhone").toString());
                    accountInfo.setFullName(inputParams.get("CHName").toString());
                    tp.addData(accountInfo);
                    BillingAddress billingAddress = new BillingAddress();
                    billingAddress.setCountryCodeISONumeric(inputParams.get("CHCountry").toString());
                    billingAddress.setState(inputParams.get("CHState").toString());
                    billingAddress.setCity(inputParams.get("CHCity").toString());
                    billingAddress.setStreetAddress(inputParams.get("CHAddress").toString());
                    billingAddress.setDetailedStreetAddress(" ");
                    billingAddress.setDetailedStreetAddressAdditional(" ");
                    billingAddress.setZipCode(inputParams.get("CHZipcode").toString());
                    tp.addData(billingAddress);
                    CreditCard creditCard = new CreditCard();
                    creditCard.setNumber(inputParams.get("CardNo").toString());
                    creditCard.setCardHolderName(inputParams.get("CHName").toString());
                    creditCard.setExpirationMonth(inputParams.get("expireMonth").toString());
                    creditCard.setExpirationYear(inputParams.get("expireYear").toString());
                    tp.addData(creditCard);
                    BrowserData browserData = new BrowserData();
                    IP ip = new IP(inputParams.get("CHIP").toString());
                    browserData.setBrowserIP(ip);
                    browserData.setBrowserAcceptHeader(inputParams.get("browserHeader").toString());
                    browserData.setBrowserUserAgent(inputParams.get("browserUserAgent").toString());
                    tp.addData(browserData);
                    MerchantRiskIndicator merchantRiskIndicator = new MerchantRiskIndicator();
                    merchantRiskIndicator.setAddressMatch(BooleanYesNo.YES);
                    merchantRiskIndicator.setAccountAge(RequestorAccountAgeType.NOT_APPLICABLE);
                    merchantRiskIndicator.setDeliveryTimeframe(DeliveryTimeframeType.TWO_DAY_OR_MORE);
                    merchantRiskIndicator.setReorderItemsInd(ReorderItemsType.FIRST_TIME);
                    merchantRiskIndicator.setShipIndicator(ShipIndicatorType.SHIP_TO_BILLING_ADDRESS);
                    tp.addData(merchantRiskIndicator);
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setMessageCategory(MessageCategoryType.PA);
                    messageInfo.setMessageType(MsgType.A_REQ);
                    messageInfo.setMessageVersion(MessageVersionType.V2_1_0);
                    tp.addData(messageInfo);
                    SDKInfo sdkInfo = new SDKInfo();
                    sdkInfo.setSdkInterface(ACSInterface.HTML_UI);
                    List<ACSUITemplate> acsUITemplateList = new ArrayList();
                    acsUITemplateList.add(ACSUITemplate.HTML_OTHER);
                    sdkInfo.setSdkUiType(acsUITemplateList);
                    sdkInfo.setSdkMaxTimeout("60");
                    tp.addData(sdkInfo);
                    TransactionInfo transactionInfo = new TransactionInfo();
                    transactionInfo.setMerchantName(inputParams.get("MerchantName").toString());
                    transactionInfo.setAcquirerBIN(inputParams.get("AcquirerBIN").toString());
                    transactionInfo.setMerchantCategoryCode(inputParams.get("MCC").toString());
                    CountryCode countryCode = new CountryCode(inputParams.get("CountryCode").toString());
                    transactionInfo.setMerchantCountryCode(countryCode);
                    transactionInfo.setAcquirerMerchantID(inputParams.get("MerchantNo").toString());
                    transactionInfo.setTransType(TxnType.PURCHASE);
                    CurrencyCode currencyCode = new CurrencyCode(inputParams.get("BillingCurr").toString());
                    transactionInfo.setCurrencyCode(currencyCode);
                    transactionInfo.setCurrencyExponent(inputParams.get("currExponent").toString());
                    transactionInfo.setPurchaseDate(purchaseDate);
                    transactionInfo.setPurchaseAmount(inputParams.get("BillingAmt").toString());
                    tp.addData(transactionInfo);
                    ThreeDSInfo threeDSInfo = new ThreeDSInfo();
                    URL threeDSRequestorURL = new URL(inputParams.get("requestUrl").toString());
                    threeDSInfo.setThreeDSRequestorURL(threeDSRequestorURL);
                    threeDSInfo.setThreeDSCompInd(TernaryYesNoUnavailable.YES);
                    threeDSInfo.setThreeDSReqAuthMethod(RequestorLoginMethodType.NO_AUTHENTICATION);
                    threeDSInfo.setThreeDSRequestorAuthenticationInd(AuthRequestType.CARDHOLDER_VERIFICATION);
                    if (inputParams.containsKey("threeDSServerTransID")) {
                        UUID uuid = new UUID(inputParams.get("threeDSServerTransID").toString());
                        threeDSInfo.setThreeDSServerTransID(uuid);
                    }

                    tp.addData(threeDSInfo);
                };
                this.transaction.results = (outcome) -> {
                    if (outcome.getTransStatus() != null && outcome.getTransStatus() == TransactionStatus.SUCCESS) {
                        log.debug("----------------------Transaction success!----------------------");
                        if (outcome.hasAuthenticationValue()) {
                            com.nds.nudetect.Base64 authValue = outcome.getAuthenticationValue();
                            String eci = outcome.getEci();
                            UUID var3 = outcome.getDsTransID();
                        }
                    }

                };
                this.assign(Flow.THREE_DS1_CHECK_ENROLLMENT_BEFORE, new Predicate() {
                    @Override
                    public boolean accept(Object o) {
                        log.debug("----------------------3ds1 callback1----------------------");
                        if (o instanceof TransactionParameters) {
                            TransactionParameters tp = (TransactionParameters) o;
                            AccountInfoData accountInfo = new AccountInfoData();
                            accountInfo.setAccountType(Constants.AccountType.NotApplicable);
                            accountInfo.setEmailAddress(inputParams.get("CHEmail").toString());
                            accountInfo.setPhone(inputParams.get("CHPhone").toString());
                            accountInfo.setFullName(inputParams.get("CHName").toString());
                            tp.addData(accountInfo);
                            BillingAddress billingAddress = new BillingAddress();
                            billingAddress.setCountryCodeISONumeric(inputParams.get("CHCountry").toString());
                            billingAddress.setState(inputParams.get("CHState").toString());
                            billingAddress.setCity(inputParams.get("CHCity").toString());
                            billingAddress.setStreetAddress(inputParams.get("CHAddress").toString());
                            billingAddress.setDetailedStreetAddress(" ");
                            billingAddress.setDetailedStreetAddressAdditional(" ");
                            billingAddress.setZipCode(inputParams.get("CHZipcode").toString());
                            tp.addData(billingAddress);
                            CreditCard creditCard = new CreditCard();
                            creditCard.setNumber(inputParams.get("CardNo").toString());
                            creditCard.setCardHolderName(inputParams.get("CHName").toString());
                            creditCard.setExpirationMonth(inputParams.get("expireMonth").toString());
                            creditCard.setExpirationYear(inputParams.get("expireYear").toString());
                            tp.addData(creditCard);
                            BrowserData browserData = new BrowserData();
                            IP ip = new IP(inputParams.get("CHIP").toString());
                            browserData.setBrowserIP(ip);
                            browserData.setBrowserAcceptHeader(inputParams.get("browserHeader").toString());
                            browserData.setBrowserUserAgent(inputParams.get("browserUserAgent").toString());
                            tp.addData(browserData);
                            MerchantRiskIndicator merchantRiskIndicator = new MerchantRiskIndicator();
                            merchantRiskIndicator.setAddressMatch(BooleanYesNo.YES);
                            merchantRiskIndicator.setAccountAge(RequestorAccountAgeType.NOT_APPLICABLE);
                            merchantRiskIndicator.setDeliveryTimeframe(DeliveryTimeframeType.TWO_DAY_OR_MORE);
                            merchantRiskIndicator.setReorderItemsInd(ReorderItemsType.FIRST_TIME);
                            merchantRiskIndicator.setShipIndicator(ShipIndicatorType.SHIP_TO_BILLING_ADDRESS);
                            tp.addData(merchantRiskIndicator);
                            MessageInfo messageInfo = new MessageInfo();
                            messageInfo.setMessageCategory(MessageCategoryType.PA);
                            messageInfo.setMessageType(MsgType.A_REQ);
                            messageInfo.setMessageVersion(MessageVersionType.V2_1_0);
                            tp.addData(messageInfo);
                            SDKInfo sdkInfo = new SDKInfo();
                            sdkInfo.setSdkInterface(ACSInterface.HTML_UI);
                            List<ACSUITemplate> acsUITemplateList = new ArrayList();
                            acsUITemplateList.add(ACSUITemplate.HTML_OTHER);
                            sdkInfo.setSdkUiType(acsUITemplateList);
                            sdkInfo.setSdkMaxTimeout("60");
                            tp.addData(sdkInfo);
                            TransactionInfo transactionInfo = new TransactionInfo();
                            transactionInfo.setMerchantName(inputParams.get("MerchantName").toString());
                            transactionInfo.setAcquirerBIN(inputParams.get("AcquirerBIN").toString());
                            transactionInfo.setMerchantCategoryCode(inputParams.get("MCC").toString());
                            CountryCode countryCode = new CountryCode(inputParams.get("CountryCode").toString());
                            transactionInfo.setMerchantCountryCode(countryCode);
                            transactionInfo.setAcquirerMerchantID(inputParams.get("MerchantNo").toString());
                            transactionInfo.setTransType(TxnType.PURCHASE);
                            CurrencyCode currencyCode = new CurrencyCode(inputParams.get("BillingCurr").toString());
                            transactionInfo.setCurrencyCode(currencyCode);
                            transactionInfo.setCurrencyExponent(inputParams.get("currExponent").toString());
                            transactionInfo.setPurchaseDate(purchaseDate);
                            transactionInfo.setPurchaseAmount(inputParams.get("BillingAmt").toString());
                            tp.addData(transactionInfo);
                            ThreeDSInfo threeDSInfo = new ThreeDSInfo();
                            URL threeDSRequestorURL = new URL(inputParams.get("requestUrl").toString());
                            threeDSInfo.setThreeDSRequestorURL(threeDSRequestorURL);
                            threeDSInfo.setThreeDSCompInd(TernaryYesNoUnavailable.YES);
                            threeDSInfo.setThreeDSReqAuthMethod(RequestorLoginMethodType.NO_AUTHENTICATION);
                            threeDSInfo.setThreeDSRequestorAuthenticationInd(AuthRequestType.CARDHOLDER_VERIFICATION);
                            tp.addData(threeDSInfo);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                this.assign(Flow.THREE_DS1_CHECK_ENROLLMENT_AFTER, o -> {
                    log.debug("----------------------3ds1 callback2----------------------");
                    if (o instanceof ResultsResponse) {
                        ResultsResponse resultsResponse = (ResultsResponse) o;
                        log.debug("callback2 response:{}!", resultsResponse.toJson());
                        return true;
                    } else {
                        return false;
                    }
                });
                this.assign(Flow.THREE_DS1_PROCESS_RESULT_BEFORE, o -> {
                    log.debug("----------------------3ds1 callback3----------------------");
                    if (o instanceof ProcessResultRequest) {
                        ProcessResultRequest processResultRequest = (ProcessResultRequest) o;
                        log.debug("callback3 response:{}", processResultRequest.toJson());
                        return true;
                    } else {
                        return false;
                    }
                });
                this.assign(Flow.THREE_DS1_TRANSACTION_RESULTS, o -> {
                    log.debug("----------------------3ds1 callback4----------------------");
                    if (o instanceof ResultsResponse) {
                        ResultsResponse response1 = (ResultsResponse) o;
                        log.debug("callback4 response:{}", response1.toJson());
                        return true;
                    } else {
                        return false;
                    }
                });
                this.assign(Flow.THREE_DS1_TRANSACTION_ERROR, o -> {
                    log.debug("----------------------3ds1 callback5----------------------");
                    if (o instanceof Error) {
                        Error err = (Error) o;
                        log.error("3ds call error!", err);
                        return true;
                    } else {
                        return false;
                    }
                });
            }
        });

        Map<String, String[]> parameterMap = request.getParameterMap();
        String requestBodyFromMap = getRequestBodyFromMap(parameterMap);
        log.debug("requestBody:{}", requestBodyFromMap);

        HttpResponse httpResponse = ndsClient.send3DSRequest(requestBodyFromMap, tp);
        log.debug("responseBody:{}", httpResponse.getBody());
        return NDSResponse.ok(httpResponse.getBody());
    }

    public static String getRequestBodyFromMap(Map<String, String[]> bodyMap) {
        StringBuilder bodyPart = new StringBuilder();

        for (Map.Entry<String, String[]> param : bodyMap.entrySet()) {
            if (bodyPart.length() != 0) {
                bodyPart.append("&");
            }

            if (!"sessionData".equals(param.getKey())) {
                try {
                    bodyPart.append(param.getKey()).append("=").append(URLEncoder.encode(((String[]) param.getValue())[0], "UTF-8"));
                } catch (UnsupportedEncodingException var5) {
                    log.warn("不支持的编码格式!", var5);
                }
            }
        }

        log.debug("bodyPart:{}", bodyPart.toString());
        return bodyPart.toString();
    }
}
