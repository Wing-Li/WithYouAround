package com.lyl.myallforyou.data;

import java.util.List;

/**
 * Created by lyl on 2017/5/23.
 */

public class NhComments {

    private int total_number;
    private boolean new_comment;
    private long group_id;
    private boolean has_more;
    private String message;
    private DataBean data;

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public boolean isNew_comment() {
        return new_comment;
    }

    public void setNew_comment(boolean new_comment) {
        this.new_comment = new_comment;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<CommentsBean> top_comments;
        private List<CommentsBean> recent_comments;
        private List<?> stick_comments;

        public List<CommentsBean> getTop_comments() {
            return top_comments;
        }

        public void setTop_comments(List<CommentsBean> top_comments) {
            this.top_comments = top_comments;
        }

        public List<CommentsBean> getRecent_comments() {
            return recent_comments;
        }

        public void setRecent_comments(List<CommentsBean> recent_comments) {
            this.recent_comments = recent_comments;
        }

        public List<?> getStick_comments() {
            return stick_comments;
        }

        public void setStick_comments(List<?> stick_comments) {
            this.stick_comments = stick_comments;
        }


        public static class CommentsBean {
            private long id;
            private int status;
            private String text;
            private long group_id;
            private int create_time;
            private long user_id;
            private String user_name;
            private String avatar_url;
            private String user_profile_url;
            private String user_profile_image_url;
            private String description;
            private String share_url;
            private boolean is_pro_user;
            private String platform;
            private String platform_id;
            private int digg_count;
            private int bury_count;
            private int user_digg;
            private int is_digg;
            private int user_bury;
            private int share_type;
            private boolean user_verified;
            private int second_level_comments_count;
            private long comment_id;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public long getGroup_id() {
                return group_id;
            }

            public void setGroup_id(long group_id) {
                this.group_id = group_id;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public long getUser_id() {
                return user_id;
            }

            public void setUser_id(long user_id) {
                this.user_id = user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getUser_profile_url() {
                return user_profile_url;
            }

            public void setUser_profile_url(String user_profile_url) {
                this.user_profile_url = user_profile_url;
            }

            public String getUser_profile_image_url() {
                return user_profile_image_url;
            }

            public void setUser_profile_image_url(String user_profile_image_url) {
                this.user_profile_image_url = user_profile_image_url;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getShare_url() {
                return share_url;
            }

            public void setShare_url(String share_url) {
                this.share_url = share_url;
            }

            public boolean isIs_pro_user() {
                return is_pro_user;
            }

            public void setIs_pro_user(boolean is_pro_user) {
                this.is_pro_user = is_pro_user;
            }

            public String getPlatform() {
                return platform;
            }

            public void setPlatform(String platform) {
                this.platform = platform;
            }

            public String getPlatform_id() {
                return platform_id;
            }

            public void setPlatform_id(String platform_id) {
                this.platform_id = platform_id;
            }

            public int getDigg_count() {
                return digg_count;
            }

            public void setDigg_count(int digg_count) {
                this.digg_count = digg_count;
            }

            public int getBury_count() {
                return bury_count;
            }

            public void setBury_count(int bury_count) {
                this.bury_count = bury_count;
            }

            public int getUser_digg() {
                return user_digg;
            }

            public void setUser_digg(int user_digg) {
                this.user_digg = user_digg;
            }

            public int getIs_digg() {
                return is_digg;
            }

            public void setIs_digg(int is_digg) {
                this.is_digg = is_digg;
            }

            public int getUser_bury() {
                return user_bury;
            }

            public void setUser_bury(int user_bury) {
                this.user_bury = user_bury;
            }

            public int getShare_type() {
                return share_type;
            }

            public void setShare_type(int share_type) {
                this.share_type = share_type;
            }

            public boolean isUser_verified() {
                return user_verified;
            }

            public void setUser_verified(boolean user_verified) {
                this.user_verified = user_verified;
            }

            public int getSecond_level_comments_count() {
                return second_level_comments_count;
            }

            public void setSecond_level_comments_count(int second_level_comments_count) {
                this.second_level_comments_count = second_level_comments_count;
            }

            public long getComment_id() {
                return comment_id;
            }

            public void setComment_id(long comment_id) {
                this.comment_id = comment_id;
            }
        }
    }
}
