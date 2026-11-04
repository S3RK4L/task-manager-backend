package uk.gov.hmcts.reform.dev.dto;

import jakarta.validation.constraints.NotBlank;
import uk.gov.hmcts.reform.dev.models.TaskStatus;

public record UpdateStatusRequest(@NotBlank(message = "Status is required") TaskStatus status) {}