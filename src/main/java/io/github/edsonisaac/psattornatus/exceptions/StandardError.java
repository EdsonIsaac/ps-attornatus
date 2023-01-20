package io.github.edsonisaac.psattornatus.exceptions;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * The type Standard error.
 */
public record StandardError(
        @NotNull Long timestamp,
        @NotNull Integer status,
        @NotEmpty String error,
        @NotEmpty String message,
        @NotEmpty String path
) implements Serializable {
}