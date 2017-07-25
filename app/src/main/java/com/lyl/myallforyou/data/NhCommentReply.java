package com.lyl.myallforyou.data;

import java.util.List;

/**
 * Created by lyl on 2017/5/23.
 */

public class NhCommentReply {

    private String message;
    private DataBeanX data;
    private boolean stable;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public boolean isStable() {
        return stable;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public static class DataBeanX {
        private boolean has_more;
        private int total_count;
        private int stick_total_number;
        private int offset;
        private boolean stick_has_more;
        private String id;
        private List<DataBean> data;
        private List<HotCommentsBean> hot_comments;

        public boolean isHas_more() {
            return has_more;
        }

        public void setHas_more(boolean has_more) {
            this.has_more = has_more;
        }

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public int getStick_total_number() {
            return stick_total_number;
        }

        public void setStick_total_number(int stick_total_number) {
            this.stick_total_number = stick_total_number;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public boolean isStick_has_more() {
            return stick_has_more;
        }

        public void setStick_has_more(boolean stick_has_more) {
            this.stick_has_more = stick_has_more;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public List<HotCommentsBean> getHot_comments() {
            return hot_comments;
        }

        public void setHot_comments(List<HotCommentsBean> hot_comments) {
            this.hot_comments = hot_comments;
        }

        public static class DataBean {
            private boolean is_owner;
            private String text;
            private int digg_count;
            private String content;
            private int create_time;
            private UserBean user;
            private boolean user_digg;
            private String id;

            public boolean isIs_owner() {
                return is_owner;
            }

            public void setIs_owner(boolean is_owner) {
                this.is_owner = is_owner;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public int getDigg_count() {
                return digg_count;
            }

            public void setDigg_count(int digg_count) {
                this.digg_count = digg_count;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public boolean isUser_digg() {
                return user_digg;
            }

            public void setUser_digg(boolean user_digg) {
                this.user_digg = user_digg;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public static class UserBean {
                private Object is_followed;
                private String description;
                private String screen_name;
                private Object is_following;
                private boolean is_blocked;
                private boolean user_verified;
                private String user_auth_info;
                private boolean is_blocking;
                private boolean is_pgc_author;
                private long user_id;
                private String name;
                private int user_relation;
                private String verified_reason;
                private String avatar_url;
                private boolean is_pro_user;
                private List<?> author_badge;
                private String id;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public Object getIs_followed() {
                    return is_followed;
                }

                public void setIs_followed(Object is_followed) {
                    this.is_followed = is_followed;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getScreen_name() {
                    return screen_name;
                }

                public void setScreen_name(String screen_name) {
                    this.screen_name = screen_name;
                }

                public Object getIs_following() {
                    return is_following;
                }

                public void setIs_following(Object is_following) {
                    this.is_following = is_following;
                }

                public boolean isIs_blocked() {
                    return is_blocked;
                }

                public void setIs_blocked(boolean is_blocked) {
                    this.is_blocked = is_blocked;
                }

                public boolean isUser_verified() {
                    return user_verified;
                }

                public void setUser_verified(boolean user_verified) {
                    this.user_verified = user_verified;
                }

                public String getUser_auth_info() {
                    return user_auth_info;
                }

                public void setUser_auth_info(String user_auth_info) {
                    this.user_auth_info = user_auth_info;
                }

                public boolean isIs_blocking() {
                    return is_blocking;
                }

                public void setIs_blocking(boolean is_blocking) {
                    this.is_blocking = is_blocking;
                }

                public boolean isIs_pgc_author() {
                    return is_pgc_author;
                }

                public void setIs_pgc_author(boolean is_pgc_author) {
                    this.is_pgc_author = is_pgc_author;
                }

                public long getUser_id() {
                    return user_id;
                }

                public void setUser_id(long user_id) {
                    this.user_id = user_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getUser_relation() {
                    return user_relation;
                }

                public void setUser_relation(int user_relation) {
                    this.user_relation = user_relation;
                }

                public String getVerified_reason() {
                    return verified_reason;
                }

                public void setVerified_reason(String verified_reason) {
                    this.verified_reason = verified_reason;
                }

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                }

                public boolean isIs_pro_user() {
                    return is_pro_user;
                }

                public void setIs_pro_user(boolean is_pro_user) {
                    this.is_pro_user = is_pro_user;
                }

                public List<?> getAuthor_badge() {
                    return author_badge;
                }

                public void setAuthor_badge(List<?> author_badge) {
                    this.author_badge = author_badge;
                }
            }
        }

        public static class HotCommentsBean {
            private boolean is_owner;
            private String text;
            private int digg_count;
            private String content;
            private int create_time;
            private UserBeanX user;
            private boolean user_digg;
            private String id;

            public boolean isIs_owner() {
                return is_owner;
            }

            public void setIs_owner(boolean is_owner) {
                this.is_owner = is_owner;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public int getDigg_count() {
                return digg_count;
            }

            public void setDigg_count(int digg_count) {
                this.digg_count = digg_count;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public UserBeanX getUser() {
                return user;
            }

            public void setUser(UserBeanX user) {
                this.user = user;
            }

            public boolean isUser_digg() {
                return user_digg;
            }

            public void setUser_digg(boolean user_digg) {
                this.user_digg = user_digg;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public static class UserBeanX {
                private Object is_followed;
                private String description;
                private String screen_name;
                private Object is_following;
                private boolean is_blocked;
                private boolean user_verified;
                private String user_auth_info;
                private boolean is_blocking;
                private boolean is_pgc_author;
                private long user_id;
                private String name;
                private int user_relation;
                private String verified_reason;
                private String avatar_url;
                private boolean is_pro_user;
                private List<?> author_badge;

                public Object getIs_followed() {
                    return is_followed;
                }

                public void setIs_followed(Object is_followed) {
                    this.is_followed = is_followed;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getScreen_name() {
                    return screen_name;
                }

                public void setScreen_name(String screen_name) {
                    this.screen_name = screen_name;
                }

                public Object getIs_following() {
                    return is_following;
                }

                public void setIs_following(Object is_following) {
                    this.is_following = is_following;
                }

                public boolean isIs_blocked() {
                    return is_blocked;
                }

                public void setIs_blocked(boolean is_blocked) {
                    this.is_blocked = is_blocked;
                }

                public boolean isUser_verified() {
                    return user_verified;
                }

                public void setUser_verified(boolean user_verified) {
                    this.user_verified = user_verified;
                }

                public String getUser_auth_info() {
                    return user_auth_info;
                }

                public void setUser_auth_info(String user_auth_info) {
                    this.user_auth_info = user_auth_info;
                }

                public boolean isIs_blocking() {
                    return is_blocking;
                }

                public void setIs_blocking(boolean is_blocking) {
                    this.is_blocking = is_blocking;
                }

                public boolean isIs_pgc_author() {
                    return is_pgc_author;
                }

                public void setIs_pgc_author(boolean is_pgc_author) {
                    this.is_pgc_author = is_pgc_author;
                }

                public long getUser_id() {
                    return user_id;
                }

                public void setUser_id(long user_id) {
                    this.user_id = user_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getUser_relation() {
                    return user_relation;
                }

                public void setUser_relation(int user_relation) {
                    this.user_relation = user_relation;
                }

                public String getVerified_reason() {
                    return verified_reason;
                }

                public void setVerified_reason(String verified_reason) {
                    this.verified_reason = verified_reason;
                }

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                }

                public boolean isIs_pro_user() {
                    return is_pro_user;
                }

                public void setIs_pro_user(boolean is_pro_user) {
                    this.is_pro_user = is_pro_user;
                }

                public List<?> getAuthor_badge() {
                    return author_badge;
                }

                public void setAuthor_badge(List<?> author_badge) {
                    this.author_badge = author_badge;
                }
            }
        }
    }
}
