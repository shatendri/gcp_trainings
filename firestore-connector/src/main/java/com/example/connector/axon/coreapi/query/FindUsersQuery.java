package com.example.connector.axon.coreapi.query;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class FindUsersQuery {
    private Map<String, String> queryParams;
}
