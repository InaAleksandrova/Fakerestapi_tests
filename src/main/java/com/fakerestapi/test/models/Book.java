package com.fakerestapi.test.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Book {

        private int id;
        private String title;
        private String description;
        private int pageCount;
        private String excerpt;
        private String publishDate;
}
