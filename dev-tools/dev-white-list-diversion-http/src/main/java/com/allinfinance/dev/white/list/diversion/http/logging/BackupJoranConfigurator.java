package com.allinfinance.dev.white.list.diversion.http.logging;

import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.joran.spi.ElementSelector;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.joran.spi.RuleStore;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * @author huanghf
 * @date 2024/3/27 15:09
 */
public class BackupJoranConfigurator extends JoranConfigurator {
    /**
     * ensure that Nacos configuration does not affect user configuration savepoints.
     *
     * @param eventList safe data
     */
    @Override
    public void registerSafeConfiguration(List<SaxEvent> eventList) {
    }

    @Override
    public void addInstanceRules(RuleStore rs) {
        super.addInstanceRules(rs);
        rs.addRule(new ElementSelector("configuration/trafficBackupProperty"), new BackupPropertyAction());
    }

    /**
     * ensure that Nacos configuration does not affect user configuration scanning url.
     *
     * @param url config url
     * @throws JoranException e
     */
    public void doBackupConfigure(URL url) throws JoranException {
        InputStream in = null;
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
            in = urlConnection.getInputStream();
            doConfigure(in, url.toExternalForm());
        } catch (IOException ioe) {
            String errMsg = "Could not open URL [" + url + "].";
            addError(errMsg, ioe);
            throw new JoranException(errMsg, ioe);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    String errMsg = "Could not close input stream";
                    addError(errMsg, ioe);
                    throw new JoranException(errMsg, ioe);
                }
            }
        }
    }
}
