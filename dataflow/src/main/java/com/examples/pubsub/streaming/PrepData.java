package com.examples.pubsub.streaming;

import com.examples.pubsub.streaming.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.bigquery.model.TableRow;
import com.google.gson.Gson;
import org.apache.beam.repackaged.core.org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.beam.sdk.transforms.DoFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class PrepData {
    public static class ToTableRow extends DoFn<UserDto, TableRow> {
        private final static Logger LOG = LoggerFactory.getLogger(ToTableRow.class);

        @ProcessElement
        public void processElement(ProcessContext c) {
            try {
                UserDto user = c.element();
                Gson gson = new Gson();
                ObjectMapper objectMapper = new ObjectMapper();
                TableRow outputRow = gson.fromJson(objectMapper.writeValueAsString(user), TableRow.class);
                LOG.debug("Timestamp overwrite to - " + LocalDateTime.now());
                outputRow.set("timestamp", LocalDateTime.now().toString());
                c.output(outputRow);
                LOG.info("Writing to BigQuery " + outputRow);
            } catch (Exception e) {
                LOG.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }
}
