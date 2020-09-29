package com.communicatorfront.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attachments {
    private Long id;
    private String fileName;
    private String filePath;
}
