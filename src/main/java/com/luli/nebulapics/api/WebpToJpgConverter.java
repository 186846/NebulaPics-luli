package com.luli.nebulapics.api;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WebpToJpgConverter {
    public static void main(String[] args) {
        String webpImageUrl = "https://luli-1319967679.cos.ap-guangzhou.myqcloud.com/space/4/2025-01-26_7uVwWUy8uX45b58y.webp";
        String outputJpgPath = "E:\\ideaproject\\complain\\NebulaPics\\src\\main\\resources\\img\\output.jpg";

        try {
            // 打开网络图片的输入流
            URL url = new URL(webpImageUrl);
            InputStream inputStream = url.openStream();

            // 读取 WebP 图片
            BufferedImage webpImage = ImageIO.read(inputStream);

            if (webpImage == null) {
                System.out.println("无法读取 WebP 图片，请检查链接或图片格式是否正确。");
                return;
            }

            // 创建输出的 JPG 文件
            File outputFile = new File(outputJpgPath);

            // 将图片保存为 JPG 格式
            ImageIO.write(webpImage, "jpg", outputFile);

            System.out.println("图片转换成功，已保存为: " + outputJpgPath);
        } catch (IOException e) {
            System.out.println("图片转换过程中出现错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}