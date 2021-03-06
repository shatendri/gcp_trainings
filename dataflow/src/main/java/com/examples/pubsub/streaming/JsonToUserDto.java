package com.examples.pubsub.streaming;

import com.examples.pubsub.streaming.dto.UserDto;
import com.examples.pubsub.streaming.dto.UserDtoValidator;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.vendor.grpc.v1p21p0.com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class JsonToUserDto extends DoFn<String, UserDto> {

    private final static Logger LOG = LoggerFactory.getLogger(JsonToUserDto.class);

    @ProcessElement
    public void processElement(ProcessContext c) {
        String entityJson = c.element();
        Gson gson = new Gson();

        UserDto userDto;

        try {
            userDto = gson.fromJson(entityJson, UserDto.class);
            if (UserDtoValidator.isUserDtoValid(userDto)) {
                userDto.setId(UUID.randomUUID().toString());
                c.output(userDto);
            } else {
                LOG.info(userDto.toString() + " is not valid");
            }
        } catch (Exception e) {
            LOG.info("Cast json to UserDto was failed:" + e.getMessage());
            e.printStackTrace();
        }

    }
}