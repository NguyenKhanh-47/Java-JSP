package com.aptech.books.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FileDisplayServlet/*")
public class FileDisplayServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB
    private String filePath;

    public void init() {
        this.filePath = getServletContext().getInitParameter("uploadPath");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestedFileName = request.getPathInfo();

        if (requestedFileName == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404
            return;
        }

        File file = new File(filePath, URLDecoder.decode(requestedFileName, "UTF-8"));

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404
            return;
        }

        String contentType = getServletContext().getMimeType(file.getName());

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");


        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
             BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE)){

            // write file to response
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        }
    }

}