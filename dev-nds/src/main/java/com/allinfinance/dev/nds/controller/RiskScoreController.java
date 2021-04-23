package com.allinfinance.dev.nds.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nds.nudetect.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Classname  com.allinfinance.dev.nds.controller.RiskScoreController
 *
 * @Description TODO
 * @Date 2021/4/2 15:39
 * @Created by ZhangYong
 */
@Slf4j
@RestController
public class RiskScoreController {
    @Value("${com.nds.nudetectEndPoint}")
    private String ndEndPoint;

    public RiskScoreController() {
    }

    @RequestMapping(
            value = {"/risk_score/"},
            produces = {"appliaction/json"}
    )
    public String index(@RequestBody JSONObject jsonParam) {
        log.debug(jsonParam.toJSONString());
        String remoteIp = jsonParam.getString("remoteIp");
        String requestUrl = jsonParam.getString("requestUrl");
        String sessionId = jsonParam.getString("sessionId");
        String userAgent = jsonParam.getString("browserUserAgent");
        String xForwardedFor = jsonParam.getString("xForwardedFor");
        String widgetData = jsonParam.getString("widgetData");
        String clientId = jsonParam.getString("clientId");
        String clientKey = jsonParam.getString("clientKey");
        NuDetectClient ndClient = null;

        try {
            ndClient = NuDetectClient.init(clientKey);
        } catch (PublisherExceptions.ClientKeyNotSet var40) {
            log.debug("error");
            var40.printStackTrace();
        }

        ndClient.setAPIBaseUrl(this.ndEndPoint);
        EnvironmentData ed = new EnvironmentData();
        ed.setRemoteIp(remoteIp);
        ed.setRequestUrl(requestUrl);
        ed.setSessionId(sessionId);
        ed.setUserAgent(userAgent);
        ed.setXForwardedFor(xForwardedFor);
        ed.setWidgetData(widgetData);
        TransactionParameters tp = null;

        try {
            tp = new TransactionParameters(ed);
        } catch (PublisherExceptions.InvalidEnvironment var39) {
            var39.printStackTrace();
        }

        tp.setPlacementString("Purchase");
        tp.setPlacementPage(1);
        AccountInfoData accountInfo = new AccountInfoData();
        accountInfo.setAccountType(Constants.AccountType.NotApplicable);
        accountInfo.setEmailAddress(jsonParam.getString("CHEmail"));
        accountInfo.setPhone(jsonParam.getString("CHPhone"));
        accountInfo.setFullName(jsonParam.getString("CHName"));
        tp.addData(accountInfo);
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setCountryCodeISONumeric(jsonParam.getString("CHCountry"));
        billingAddress.setState(jsonParam.getString("CHState"));
        billingAddress.setCity(jsonParam.getString("CHCity"));
        billingAddress.setStreetAddress(jsonParam.getString("CHAddress"));
        billingAddress.setDetailedStreetAddress(" ");
        billingAddress.setDetailedStreetAddressAdditional(" ");
        billingAddress.setZipCode(jsonParam.getString("CHZipcode"));
        tp.addData(billingAddress);
        CreditCard creditCard = new CreditCard();
        creditCard.setNumber(jsonParam.getString("CardNo"));
        creditCard.setCardHolderName(jsonParam.getString("CHName"));
        creditCard.setExpirationMonth(jsonParam.getString("expireMonth"));
        creditCard.setExpirationYear(jsonParam.getString("expireYear"));
        tp.addData(creditCard);
        BrowserData browserData = new BrowserData();
        IP ip = new IP(jsonParam.getString("CHIP"));
        browserData.setBrowserIP(ip);
        browserData.setBrowserAcceptHeader(jsonParam.getString("browserHeader"));
        browserData.setBrowserUserAgent(jsonParam.getString("browserUserAgent"));
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
        transactionInfo.setMerchantName(jsonParam.getString("MerchantName"));
        transactionInfo.setAcquirerBIN(jsonParam.getString("AcquirerBIN"));
        transactionInfo.setMerchantCategoryCode(jsonParam.getString("MCC"));
        CountryCode countryCode = new CountryCode(jsonParam.getString("CountryCode"));
        transactionInfo.setMerchantCountryCode(countryCode);
        transactionInfo.setAcquirerMerchantID(jsonParam.getString("MerchantNo"));
        transactionInfo.setTransType(TxnType.PURCHASE);
        CurrencyCode currencyCode = new CurrencyCode(jsonParam.getString("BillingCurr"));
        transactionInfo.setCurrencyCode(currencyCode);
        transactionInfo.setCurrencyExponent(jsonParam.getString("currExponent"));
        Date purchaseDate = new Date(Calendar.getInstance());
        transactionInfo.setPurchaseDate(purchaseDate);
        transactionInfo.setPurchaseAmount(jsonParam.getString("BillingAmt"));
        tp.addData(transactionInfo);
        CartProduct cp = new CartProduct();
        cp.setCurrencyCode(jsonParam.getString("OrderCurr"));
        cp.setProductPrice(jsonParam.getString("OrderAmt"));
        cp.setProductQuantity(jsonParam.getString("Qantity"));
        tp.addData(cp);
        ThreeDSInfo threeDSInfo = new ThreeDSInfo();
        URL threeDSRequestorURL = new URL(requestUrl);
        threeDSInfo.setThreeDSRequestorURL(threeDSRequestorURL);
        threeDSInfo.setThreeDSCompInd(TernaryYesNoUnavailable.YES);
        threeDSInfo.setThreeDSReqAuthMethod(RequestorLoginMethodType.NO_AUTHENTICATION);
        threeDSInfo.setThreeDSRequestorAuthenticationInd(AuthRequestType.CARDHOLDER_VERIFICATION);
        tp.addData(threeDSInfo);
        tp.enableIDCIMode();
        NuDetectScoreResponse scoreRes = ndClient.score(tp);
        HashMap<String, String> map = new HashMap();
        map.put("msgCode", "0000");
        map.put("msg", "Processing Successfully");
        log.debug("Request ID: " + scoreRes.getRequestID() + " | Session ID: " + scoreRes.getWebSessionID() + " | NuDetect Status: " + scoreRes.getStatusCode() + " – " + scoreRes.getStatusMessage() + "\r\n");
        if (scoreRes.getStatusCode() != 200) {
            map.put("msgCode", "9000");
            map.put("msg", "Satus Code: " + scoreRes.getStatusCode() + " – " + scoreRes.getStatusMessage());
            log.debug("Request ID: " + scoreRes.getRequestID() + " | Session ID: " + scoreRes.getWebSessionID() + " | NuDetect Status: " + scoreRes.getStatusCode() + " – " + scoreRes.getStatusMessage());
        } else {
            NuDetectScoreComponents scoreComp = scoreRes.getScoreComponents();
            int totalScore = scoreComp.getTotalScore();
            int positiveScore = scoreComp.getPositiveScore();
            int riskScore = scoreComp.getRiskScore();
            int policyScore = scoreComp.getPolicyScore();
            Constants.ScoreBand scoreBand = scoreRes.getScoreBand();
            boolean threeDResult = scoreRes.has3DSResponse();
            String interdictionType = scoreRes.getInterdictionType().getValue();
            log.debug("interdictionType: " + interdictionType);
            log.debug("interdictionType: " + interdictionType);
            if ("block".equals(interdictionType)) {
                log.debug("block ....");
            } else if ("3ds2".equals(interdictionType)) {
                log.debug("3ds2 ....");
            } else if ("proceed".equals(interdictionType)) {
                log.debug("proceed ....");
            }

            map.put("totalScore", Integer.toString(totalScore));
            map.put("positiveScore", Integer.toString(positiveScore));
            map.put("riskScore", Integer.toString(riskScore));
            map.put("policyScore", Integer.toString(policyScore));
            map.put("scoreBand", scoreBand.toString());
            map.put("threeDResult", String.valueOf(threeDResult));
            map.put("interdictionType", interdictionType);
            log.debug("totalScore: " + totalScore);
            log.debug("positiveScore: " + positiveScore);
            log.debug("policyScore: " + policyScore);
            log.debug("riskScore: " + riskScore);
            log.debug("scoreBand: " + scoreBand.toString());
            log.debug("threeDResult: " + String.valueOf(threeDResult));
            log.debug("interdictionType: " + interdictionType);
            log.debug("positiveScore: " + positiveScore);
            log.debug("riskScore: " + riskScore);
            log.debug("policyScore: " + policyScore);
            log.debug("scoreBand: " + scoreBand);
            log.debug("3Dresult: " + scoreRes.has3DSResponse());
            log.debug("interdictionType: " + interdictionType);
        }

        return JSON.toJSONString(map);
    }
}
