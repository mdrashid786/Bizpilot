package com.bizpilot.business.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardResponse {

    private BusinessResponse business;

    private boolean businessCreated;

}