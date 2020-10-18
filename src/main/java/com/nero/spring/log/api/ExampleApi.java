package com.nero.spring.log.api;

import com.nero.spring.log.dto.ExampleDTO;
import com.nero.spring.log.vo.ExampleVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(value = "v1/example")
public class ExampleApi {

    @PostMapping(value = "getUUIDString")
    public ExampleVO getUUIDString(@RequestBody ExampleDTO dto) {

        return new ExampleVO(UUID.randomUUID().toString(), new Date());

    }

    @PostMapping(value = "getTxt")
    public void getTxt(@RequestBody ExampleDTO dto, HttpServletResponse response) throws IOException {
        byte[] result = "你好".getBytes();
        try (OutputStream os = response.getOutputStream()) {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + new Date().getTime() + ".txt");
            response.setContentType("text/plain; charset=utf-8");
            os.write(result);
            os.flush();
        }

    }

    @PostMapping(value = "getQueryString")
    public String getTxt(@RequestParam String queryString, @RequestParam String queryString1) throws IOException {
        System.out.println(queryString);
        System.out.println(queryString1);
        return UUID.randomUUID().toString();
    }

    @PostMapping(value = "pathVariable/{name}/{age}")
    public String pathVariable(@PathVariable String name, @PathVariable Integer age, @RequestBody ExampleDTO dto) throws IOException {
        System.out.println(name);
        System.out.println(age);
        return UUID.randomUUID().toString();
    }

    @PostMapping(value = "uploadTxt")
    public String uploadTxt(@RequestParam("file")MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        System.out.println(new String(bytes, StandardCharsets.UTF_8));

        return UUID.randomUUID().toString();
    }

}
