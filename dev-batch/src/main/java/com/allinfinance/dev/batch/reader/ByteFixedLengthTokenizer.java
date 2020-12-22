package com.allinfinance.dev.batch.reader;

import org.springframework.batch.item.file.transform.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ByteFixedLengthTokenizer
 *
 * @author jiangjf
 * @date 2018/4/3
 */
public class ByteFixedLengthTokenizer extends AbstractLineTokenizer {
    private String encoding = Charset.defaultCharset().name();

    private Range[] ranges;

    private int maxRange = 0;

    private boolean open = false;

    /**
     * Set the column ranges. Used in conjunction with the
     * {@link RangeArrayPropertyEditor} this property can be set in the form of
     * a String describing the range boundaries, e.g. "1,4,7" or "1-3,4-6,7" or
     * "1-2,4-5,7-10". If the last range is open then the rest of the line is
     * read into that column (irrespective of the strict flag setting).
     *
     * @param ranges the column ranges expected in the input
     * @see #setStrict(boolean)
     */
    public void setColumns(Range[] ranges) {
        this.ranges = Arrays.asList(ranges).toArray(new Range[ranges.length]);
        calculateMaxRange(ranges);
    }

    /*
     * Calculate the highest value within an array of ranges. The ranges aren't
     * necessarily in order. For example: "5-10, 1-4,11-15". Furthermore, there
     * isn't always a min and max, such as: "1,4-20, 22"
     */
    private void calculateMaxRange(Range[] ranges) {
        if (ranges == null || ranges.length == 0) {
            maxRange = 0;
            return;
        }

        open = false;
        maxRange = ranges[0].getMin();

        for (int i = 0; i < ranges.length; i++) {
            int upperBound;
            if (ranges[i].hasMaxValue()) {
                upperBound = ranges[i].getMax();
            } else {
                upperBound = ranges[i].getMin();
                if (upperBound > maxRange) {
                    open = true;
                }
            }

            if (upperBound > maxRange) {
                maxRange = upperBound;
            }
        }
    }

    /**
     * Yields the tokens resulting from the splitting of the supplied
     * <code>line</code>.
     *
     * @param line the line to be tokenized (can be <code>null</code>)
     * @return the resulting tokens (empty if the line is null)
     * @throws IncorrectLineLengthException if line length is greater than or
     *                                      less than the max range set.
     */
    @Override
    protected List<String> doTokenize(String line) {
        List<String> tokens = new ArrayList<String>(ranges.length);
        int lineLength;
        String token;
        byte[] bytes;

        try {
            bytes = line.getBytes(encoding);
            lineLength = bytes.length;
        } catch (UnsupportedEncodingException e) {
            throw new FlatFileFormatException("文件编码格式不支持!");
        }

        if (lineLength < maxRange && isStrict()) {
            throw new IncorrectLineLengthException("Line is shorter than max range " + maxRange, maxRange, lineLength, line);
        }

        if (!open && lineLength > maxRange && isStrict()) {
            throw new IncorrectLineLengthException("Line is longer than max range " + maxRange, maxRange, lineLength, line);
        }

        for (Range range : ranges) {

            int startPos = range.getMin() - 1;
            int endPos = range.getMax();

            try {
                if (lineLength >= endPos) {
                    token = new String(Arrays.copyOfRange(bytes, startPos, endPos), encoding);
                } else if (lineLength >= startPos) {
                    token = new String(Arrays.copyOfRange(bytes, startPos, lineLength), encoding);
                } else {
                    token = "";
                }

            } catch (UnsupportedEncodingException e) {
                throw new FlatFileFormatException("文件编码格式不支持!");
            }

            tokens.add(token);
        }

        return tokens;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
