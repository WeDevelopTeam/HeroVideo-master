package com.github.bigexcalibur.herovideo.mvp.video.model;

import java.util.List;

/**
 * Created by Xie.Zhou on 2017/1/20.
 */


public class Video {
    /**
     * content :
     * <p>
     * durl : [{"content":"\n\t","order":1,"size":77163745,"length":348227,"backup_url":{"content":"\n\t\t","url":[{"content":"http://cn-gdjm1-dx.acgvideo.com/vg6/e/25/2852377-1.flv?expires=1484910300&ssig=ZMubHxpJB4L_oOuQSKBokw&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"},{"content":"http://ws.acgvideo.com/1/47/2852377-1.flv?wsTime=1484910497&wsSecret2=d39500c56a8fc813798db410b47c647c&oi=1902339148&rate=1500"}]},"url":"http://cn-hbjz5-dx.acgvideo.com/vg7/8/4b/2852377-1.flv?expires=1484910300&ssig=4g5OmB_3hd7i-sgiAY06CQ&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"},{"content":"\n\t","size":83365146,"order":2,"length":411456,"url":"http://cn-hbjz5-dx.acgvideo.com/vg7/8/4b/2852377-2.flv?expires=1484910300&ssig=cUACHcrxeV9IIHNMtM4GNQ&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"},{"content":"\n\t","size":75933060,"order":3,"length":314261,"url":"http://cn-hbjz5-dx.acgvideo.com/vg7/8/4b/2852377-3.flv?expires=1484910300&ssig=Bkr-0EBzYuQNJQU8ehY0HQ&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"},{"content":"\n\t","size":60877038,"order":4,"length":268247,"url":"http://cn-hbjz5-dx.acgvideo.com/vg7/8/4b/2852377-4.flv?expires=1484910300&ssig=7BWgqA3gp_tzjLkCSlK-_g&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"},{"content":"\n\t","size":90795086,"order":5,"length":457536,"url":"http://cn-hbjz5-dx.acgvideo.com/vg7/8/4b/2852377-5.flv?expires=1484910300&ssig=tuv1-pCKhmwkkk-8dB633g&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"},{"content":"\n\t","size":92647465,"order":6,"length":417963,"url":"http://cn-hbjz5-dx.acgvideo.com/vg7/8/4b/2852377-6.flv?expires=1484910300&ssig=HxEOsb4mfn4b1e8PPnv4FA&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"},{"content":"\n\t","size":63507640,"order":7,"length":316928,"url":"http://cn-hbjz5-dx.acgvideo.com/vg7/8/4b/2852377-7.flv?expires=1484910300&ssig=VDr6Xs_xECsaQe9aKgbLBQ&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"},{"content":"\n\t","size":51438240,"order":8,"length":260138,"url":"http://cn-hbjz5-dx.acgvideo.com/vg7/8/4b/2852377-8.flv?expires=1484910300&ssig=Ulj-4bGaxvrHkxfl7KHZhA&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"},{"content":"\n\t","size":75579683,"order":9,"length":427754,"url":"http://cn-hbjz5-dx.acgvideo.com/vg7/8/4b/2852377-9.flv?expires=1484910300&ssig=MrByaYQbo6mlwbH1j8J6KQ&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"}]
     * seek_param : start
     * accept_quality : 3,2,1
     * timelength : 3222510
     * seek_type : offset
     * from : local
     * accept_format : mp4,hdmp4,flv
     * result : suee
     * format : flv
     */

    private String content;
    private String seek_param;
    private String accept_quality;
    private int timelength;
    private String seek_type;
    private String from;
    private String accept_format;
    private String result;
    private String format;
    private List<Durl> durl;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSeek_param() {
        return seek_param;
    }

    public void setSeek_param(String seek_param) {
        this.seek_param = seek_param;
    }

    public String getAccept_quality() {
        return accept_quality;
    }

    public void setAccept_quality(String accept_quality) {
        this.accept_quality = accept_quality;
    }

    public int getTimelength() {
        return timelength;
    }

    public void setTimelength(int timelength) {
        this.timelength = timelength;
    }

    public String getSeek_type() {
        return seek_type;
    }

    public void setSeek_type(String seek_type) {
        this.seek_type = seek_type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAccept_format() {
        return accept_format;
    }

    public void setAccept_format(String accept_format) {
        this.accept_format = accept_format;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<Durl> getDurl() {
        return durl;
    }

    public void setDurl(List<Durl> durl) {
        this.durl = durl;
    }

    public static class Durl {
        /**
         * content :
         * <p>
         * order : 1
         * size : 77163745
         * length : 348227
         * backup_url : {"content":"\n\t\t","url":[{"content":"http://cn-gdjm1-dx.acgvideo.com/vg6/e/25/2852377-1.flv?expires=1484910300&ssig=ZMubHxpJB4L_oOuQSKBokw&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"},{"content":"http://ws.acgvideo.com/1/47/2852377-1.flv?wsTime=1484910497&wsSecret2=d39500c56a8fc813798db410b47c647c&oi=1902339148&rate=1500"}]}
         * url : http://cn-hbjz5-dx.acgvideo.com/vg7/8/4b/2852377-1.flv?expires=1484910300&ssig=4g5OmB_3hd7i-sgiAY06CQ&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1
         */

        private String content;
        private int order;
        private int size;
        private int length;
        private BackupUrl backup_url;
        private String url;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public BackupUrl getBackup_url() {
            return backup_url;
        }

        public void setBackup_url(BackupUrl backup_url) {
            this.backup_url = backup_url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public static class BackupUrl {
            /**
             * content :
             * <p>
             * url : [{"content":"http://cn-gdjm1-dx.acgvideo.com/vg6/e/25/2852377-1.flv?expires=1484910300&ssig=ZMubHxpJB4L_oOuQSKBokw&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1"},{"content":"http://ws.acgvideo.com/1/47/2852377-1.flv?wsTime=1484910497&wsSecret2=d39500c56a8fc813798db410b47c647c&oi=1902339148&rate=1500"}]
             */

            private String content;
            private List<UrlBean> url;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public List<UrlBean> getUrl() {
                return url;
            }

            public void setUrl(List<UrlBean> url) {
                this.url = url;
            }

            public static class UrlBean {
                /**
                 * content : http://cn-gdjm1-dx.acgvideo.com/vg6/e/25/2852377-1.flv?expires=1484910300&ssig=ZMubHxpJB4L_oOuQSKBokw&oi=1902339148&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1
                 */

                private String content;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Video{" +
                "content='" + content + '\'' +
                ", seek_param='" + seek_param + '\'' +
                ", accept_quality='" + accept_quality + '\'' +
                ", timelength=" + timelength +
                ", seek_type='" + seek_type + '\'' +
                ", from='" + from + '\'' +
                ", accept_format='" + accept_format + '\'' +
                ", result='" + result + '\'' +
                ", format='" + format + '\'' +
                ", durl=" + durl +
                '}';
    }
}
