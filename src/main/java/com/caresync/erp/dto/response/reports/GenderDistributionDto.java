package com.caresync.erp.dto.response.reports;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenderDistributionDto {
    private Long male;
    private Long female;
    private Long other;
    private Long total;
}