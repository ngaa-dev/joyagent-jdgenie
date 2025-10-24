package com.jd.genie.sse.v2;


public class StreamingLogger {

    public void systemOutPrint() {
        // 使用ANSI控制码实现覆盖效果
        for (int i = 0; i <= 100; i++) {
            // \033[2K 清除当前行
            // \r 回到行首
            System.out.print("\033[2K\r 处理进度: " + i + "%");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 最后换行
        System.out.println();
    }

    public void dynamicSpaceLogger() {
        int maxLength = 0;
        for (int i = 0; i <= 100; i++) {
            String message = "处理进度: " + i + "%";
            maxLength = Math.max(maxLength, message.length());
            // 构建足够的空格来覆盖之前的输出
            StringBuilder spaces = new StringBuilder();
            for (int j = 0; j < maxLength; j++) {
                spaces.append(" ");
            }
            System.out.print("\r" + message + spaces.toString());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        StreamingLogger streamingLogger = new StreamingLogger();
//        streamingLogger.systemOutPrint();
        streamingLogger.dynamicSpaceLogger();
    }
}
