package com.example.aws_s3_storage_api;

import com.example.aws_s3_storage_api.controller.FileController;
import com.example.aws_s3_storage_api.service.FileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
class FileControllerSearchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    void searchFilesTest() throws Exception {
        // Mocked service response
        List<String> mockResponse = Arrays.asList("file1.pdf", "file2.docx");
        when(fileService.searchFiles("user123")).thenReturn(mockResponse);

        // Perform GET request and verify response
        mockMvc.perform(get("/api/storage/search?userName=user123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("file1.pdf"))
                .andExpect(jsonPath("$[1]").value("file2.docx"));

        // Verify service was called
        Mockito.verify(fileService).searchFiles("user123");
    }
}
