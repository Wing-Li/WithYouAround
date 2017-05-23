package com.lyl.myallforyou.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lyl on 2017/5/8.
 */

public class NhEassay {

    private String message;
    private DataBeanX data;

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

    public static class DataBeanX {
        private boolean has_more;
        private String tip;
        private boolean has_new_message;
        private String max_time;
        private String min_time;
        private List<DataBean> data;

        public boolean isHas_more() {
            return has_more;
        }

        public void setHas_more(boolean has_more) {
            this.has_more = has_more;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public boolean isHas_new_message() {
            return has_new_message;
        }

        public void setHas_new_message(boolean has_new_message) {
            this.has_new_message = has_new_message;
        }

        public String getMax_time() {
            return max_time;
        }

        public void setMax_time(String max_time) {
            this.max_time = max_time;
        }

        public String getMin_time() {
            return min_time;
        }

        public void setMin_time(String min_time) {
            this.min_time = min_time;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            private GroupBean group;
            private String type;
            private String display_time;
            private String online_time;
            private AdBean ad;
            private List<CommentsBean> comments;

            public GroupBean getGroup() {
                return group;
            }

            public void setGroup(GroupBean group) {
                this.group = group;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDisplay_time() {
                return display_time;
            }

            public void setDisplay_time(String display_time) {
                this.display_time = display_time;
            }

            public String getOnline_time() {
                return online_time;
            }

            public void setOnline_time(String online_time) {
                this.online_time = online_time;
            }

            public AdBean getAd() {
                return ad;
            }

            public void setAd(AdBean ad) {
                this.ad = ad;
            }

            public List<CommentsBean> getComments() {
                return comments;
            }

            public void setComments(List<CommentsBean> comments) {
                this.comments = comments;
            }

            public static class GroupBean {
                private UserBean user;
                private String text;
                private String neihan_hot_start_time;
                private long create_time;
                private long id;
                private String favorite_count;
                private String go_detail_count;
                private String user_favorite;
                private String share_type;
                private double max_screen_width_percent;
                private String is_can_share;
                private String comment_count;
                private String share_url;
                private String label;
                private String content;
                private String category_type;
                private String id_str;
                private String media_type;
                private String share_count;
                private String type;
                private String status;
                private String has_comments;
                private LargeImageBean large_image;
                private String user_bury;
                private ActivityBean activity;
                private String status_desc;
                private boolean quick_comment;
                private String display_type;
                private String neihan_hot_end_time;
                private int is_gif;
                private String user_digg;
                private String online_time;
                private String category_name;
                private boolean category_visible;
                private String bury_count;
                private boolean is_anonymous;
                private String repin_count;
                private double min_screen_width_percent;
                private boolean is_neihan_hot;
                private String digg_count;
                private GifvideoBean gifvideo;
                private String has_hot_comments;
                private boolean allow_dislike;
                private String image_status;
                private String user_repin;
                private NeihanHotLinkBean neihan_hot_link;
                private long group_id;
                private MiddleImageBean middle_image;
                private String category_id;
                private List<LargeImageListBean> large_image_list;
                private List<DislikeReasonBean> dislike_reason;
                private List<ThumbImageListBean> thumb_image_list;

                public UserBean getUser() {
                    return user;
                }

                public void setUser(UserBean user) {
                    this.user = user;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public String getNeihan_hot_start_time() {
                    return neihan_hot_start_time;
                }

                public void setNeihan_hot_start_time(String neihan_hot_start_time) {
                    this.neihan_hot_start_time = neihan_hot_start_time;
                }

                public long getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(long create_time) {
                    this.create_time = create_time;
                }

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public String getFavorite_count() {
                    return favorite_count;
                }

                public void setFavorite_count(String favorite_count) {
                    this.favorite_count = favorite_count;
                }

                public String getGo_detail_count() {
                    return go_detail_count;
                }

                public void setGo_detail_count(String go_detail_count) {
                    this.go_detail_count = go_detail_count;
                }

                public String getUser_favorite() {
                    return user_favorite;
                }

                public void setUser_favorite(String user_favorite) {
                    this.user_favorite = user_favorite;
                }

                public String getShare_type() {
                    return share_type;
                }

                public void setShare_type(String share_type) {
                    this.share_type = share_type;
                }

                public double getMax_screen_width_percent() {
                    return max_screen_width_percent;
                }

                public void setMax_screen_width_percent(double max_screen_width_percent) {
                    this.max_screen_width_percent = max_screen_width_percent;
                }

                public String getIs_can_share() {
                    return is_can_share;
                }

                public void setIs_can_share(String is_can_share) {
                    this.is_can_share = is_can_share;
                }

                public String getComment_count() {
                    return comment_count;
                }

                public void setComment_count(String comment_count) {
                    this.comment_count = comment_count;
                }

                public String getShare_url() {
                    return share_url;
                }

                public void setShare_url(String share_url) {
                    this.share_url = share_url;
                }

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getCategory_type() {
                    return category_type;
                }

                public void setCategory_type(String category_type) {
                    this.category_type = category_type;
                }

                public String getId_str() {
                    return id_str;
                }

                public void setId_str(String id_str) {
                    this.id_str = id_str;
                }

                public String getMedia_type() {
                    return media_type;
                }

                public void setMedia_type(String media_type) {
                    this.media_type = media_type;
                }

                public String getShare_count() {
                    return share_count;
                }

                public void setShare_count(String share_count) {
                    this.share_count = share_count;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getHas_comments() {
                    return has_comments;
                }

                public void setHas_comments(String has_comments) {
                    this.has_comments = has_comments;
                }

                public LargeImageBean getLarge_image() {
                    return large_image;
                }

                public void setLarge_image(LargeImageBean large_image) {
                    this.large_image = large_image;
                }

                public String getUser_bury() {
                    return user_bury;
                }

                public void setUser_bury(String user_bury) {
                    this.user_bury = user_bury;
                }

                public ActivityBean getActivity() {
                    return activity;
                }

                public void setActivity(ActivityBean activity) {
                    this.activity = activity;
                }

                public String getStatus_desc() {
                    return status_desc;
                }

                public void setStatus_desc(String status_desc) {
                    this.status_desc = status_desc;
                }

                public boolean isQuick_comment() {
                    return quick_comment;
                }

                public void setQuick_comment(boolean quick_comment) {
                    this.quick_comment = quick_comment;
                }

                public String getDisplay_type() {
                    return display_type;
                }

                public void setDisplay_type(String display_type) {
                    this.display_type = display_type;
                }

                public String getNeihan_hot_end_time() {
                    return neihan_hot_end_time;
                }

                public void setNeihan_hot_end_time(String neihan_hot_end_time) {
                    this.neihan_hot_end_time = neihan_hot_end_time;
                }

                public int getIs_gif() {
                    return is_gif;
                }

                public void setIs_gif(int is_gif) {
                    this.is_gif = is_gif;
                }

                public String getUser_digg() {
                    return user_digg;
                }

                public void setUser_digg(String user_digg) {
                    this.user_digg = user_digg;
                }

                public String getOnline_time() {
                    return online_time;
                }

                public void setOnline_time(String online_time) {
                    this.online_time = online_time;
                }

                public String getCategory_name() {
                    return category_name;
                }

                public void setCategory_name(String category_name) {
                    this.category_name = category_name;
                }

                public boolean isCategory_visible() {
                    return category_visible;
                }

                public void setCategory_visible(boolean category_visible) {
                    this.category_visible = category_visible;
                }

                public String getBury_count() {
                    return bury_count;
                }

                public void setBury_count(String bury_count) {
                    this.bury_count = bury_count;
                }

                public boolean isIs_anonymous() {
                    return is_anonymous;
                }

                public void setIs_anonymous(boolean is_anonymous) {
                    this.is_anonymous = is_anonymous;
                }

                public String getRepin_count() {
                    return repin_count;
                }

                public void setRepin_count(String repin_count) {
                    this.repin_count = repin_count;
                }

                public double getMin_screen_width_percent() {
                    return min_screen_width_percent;
                }

                public void setMin_screen_width_percent(double min_screen_width_percent) {
                    this.min_screen_width_percent = min_screen_width_percent;
                }

                public boolean isIs_neihan_hot() {
                    return is_neihan_hot;
                }

                public void setIs_neihan_hot(boolean is_neihan_hot) {
                    this.is_neihan_hot = is_neihan_hot;
                }

                public String getDigg_count() {
                    return digg_count;
                }

                public void setDigg_count(String digg_count) {
                    this.digg_count = digg_count;
                }

                public GifvideoBean getGifvideo() {
                    return gifvideo;
                }

                public void setGifvideo(GifvideoBean gifvideo) {
                    this.gifvideo = gifvideo;
                }

                public String getHas_hot_comments() {
                    return has_hot_comments;
                }

                public void setHas_hot_comments(String has_hot_comments) {
                    this.has_hot_comments = has_hot_comments;
                }

                public boolean isAllow_dislike() {
                    return allow_dislike;
                }

                public void setAllow_dislike(boolean allow_dislike) {
                    this.allow_dislike = allow_dislike;
                }

                public String getImage_status() {
                    return image_status;
                }

                public void setImage_status(String image_status) {
                    this.image_status = image_status;
                }

                public String getUser_repin() {
                    return user_repin;
                }

                public void setUser_repin(String user_repin) {
                    this.user_repin = user_repin;
                }

                public NeihanHotLinkBean getNeihan_hot_link() {
                    return neihan_hot_link;
                }

                public void setNeihan_hot_link(NeihanHotLinkBean neihan_hot_link) {
                    this.neihan_hot_link = neihan_hot_link;
                }

                public long getGroup_id() {
                    return group_id;
                }

                public void setGroup_id(long group_id) {
                    this.group_id = group_id;
                }

                public MiddleImageBean getMiddle_image() {
                    return middle_image;
                }

                public void setMiddle_image(MiddleImageBean middle_image) {
                    this.middle_image = middle_image;
                }

                public String getCategory_id() {
                    return category_id;
                }

                public void setCategory_id(String category_id) {
                    this.category_id = category_id;
                }

                public List<LargeImageListBean> getLarge_image_list() {
                    return large_image_list;
                }

                public void setLarge_image_list(List<LargeImageListBean> large_image_list) {
                    this.large_image_list = large_image_list;
                }

                public List<ThumbImageListBean> getThumb_image_list() {
                    return thumb_image_list;
                }

                public void setThumb_image_list(List<ThumbImageListBean> thumb_image_list) {
                    this.thumb_image_list = thumb_image_list;
                }

                public List<DislikeReasonBean> getDislike_reason() {
                    return dislike_reason;
                }

                public void setDislike_reason(List<DislikeReasonBean> dislike_reason) {
                    this.dislike_reason = dislike_reason;
                }

                public static class UserBean {
                    private long user_id;
                    private String name;
                    private String followings;
                    private boolean user_verified;
                    private String ugc_count;
                    private String avatar_url;
                    private String followers;
                    private boolean is_following;
                    private boolean is_pro_user;

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

                    public String getFollowings() {
                        return followings;
                    }

                    public void setFollowings(String followings) {
                        this.followings = followings;
                    }

                    public boolean isUser_verified() {
                        return user_verified;
                    }

                    public void setUser_verified(boolean user_verified) {
                        this.user_verified = user_verified;
                    }

                    public String getUgc_count() {
                        return ugc_count;
                    }

                    public void setUgc_count(String ugc_count) {
                        this.ugc_count = ugc_count;
                    }

                    public String getAvatar_url() {
                        return avatar_url;
                    }

                    public void setAvatar_url(String avatar_url) {
                        this.avatar_url = avatar_url;
                    }

                    public String getFollowers() {
                        return followers;
                    }

                    public void setFollowers(String followers) {
                        this.followers = followers;
                    }

                    public boolean isIs_following() {
                        return is_following;
                    }

                    public void setIs_following(boolean is_following) {
                        this.is_following = is_following;
                    }

                    public boolean isIs_pro_user() {
                        return is_pro_user;
                    }

                    public void setIs_pro_user(boolean is_pro_user) {
                        this.is_pro_user = is_pro_user;
                    }
                }

                public static class LargeImageBean {
                    private String width;
                    private String r_height;
                    private String r_width;
                    private String uri;
                    private String height;
                    private List<UrlListBean> url_list;

                    public String getWidth() {
                        return width;
                    }

                    public void setWidth(String width) {
                        this.width = width;
                    }

                    public String getR_height() {
                        return r_height;
                    }

                    public void setR_height(String r_height) {
                        this.r_height = r_height;
                    }

                    public String getR_width() {
                        return r_width;
                    }

                    public void setR_width(String r_width) {
                        this.r_width = r_width;
                    }

                    public String getUri() {
                        return uri;
                    }

                    public void setUri(String uri) {
                        this.uri = uri;
                    }

                    public String getHeight() {
                        return height;
                    }

                    public void setHeight(String height) {
                        this.height = height;
                    }

                    public List<UrlListBean> getUrl_list() {
                        return url_list;
                    }

                    public void setUrl_list(List<UrlListBean> url_list) {
                        this.url_list = url_list;
                    }

                    public static class UrlListBean {
                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }

                public static class ActivityBean {
                }

                public static class GifvideoBean {
                    @SerializedName("360p_video")
                    private _$360pVideoBean _$360p_video;
                    private OriginVideoBean origin_video;
                    private String video_id;
                    @SerializedName("720p_video")
                    private _$720pVideoBean _$720p_video;
                    private String mp4_url;
                    private String video_height;
                    @SerializedName("480p_video")
                    private _$480pVideoBean _$480p_video;
                    private String cover_image_uri;
                    private double duration;
                    private String video_width;

                    public _$360pVideoBean get_$360p_video() {
                        return _$360p_video;
                    }

                    public void set_$360p_video(_$360pVideoBean _$360p_video) {
                        this._$360p_video = _$360p_video;
                    }

                    public OriginVideoBean getOrigin_video() {
                        return origin_video;
                    }

                    public void setOrigin_video(OriginVideoBean origin_video) {
                        this.origin_video = origin_video;
                    }

                    public String getVideo_id() {
                        return video_id;
                    }

                    public void setVideo_id(String video_id) {
                        this.video_id = video_id;
                    }

                    public _$720pVideoBean get_$720p_video() {
                        return _$720p_video;
                    }

                    public void set_$720p_video(_$720pVideoBean _$720p_video) {
                        this._$720p_video = _$720p_video;
                    }

                    public String getMp4_url() {
                        return mp4_url;
                    }

                    public void setMp4_url(String mp4_url) {
                        this.mp4_url = mp4_url;
                    }

                    public String getVideo_height() {
                        return video_height;
                    }

                    public void setVideo_height(String video_height) {
                        this.video_height = video_height;
                    }

                    public _$480pVideoBean get_$480p_video() {
                        return _$480p_video;
                    }

                    public void set_$480p_video(_$480pVideoBean _$480p_video) {
                        this._$480p_video = _$480p_video;
                    }

                    public String getCover_image_uri() {
                        return cover_image_uri;
                    }

                    public void setCover_image_uri(String cover_image_uri) {
                        this.cover_image_uri = cover_image_uri;
                    }

                    public double getDuration() {
                        return duration;
                    }

                    public void setDuration(double duration) {
                        this.duration = duration;
                    }

                    public String getVideo_width() {
                        return video_width;
                    }

                    public void setVideo_width(String video_width) {
                        this.video_width = video_width;
                    }

                    public static class _$360pVideoBean {
                        private String width;
                        private String uri;
                        private String height;
                        private List<UrlListBeanX> url_list;

                        public String getWidth() {
                            return width;
                        }

                        public void setWidth(String width) {
                            this.width = width;
                        }

                        public String getUri() {
                            return uri;
                        }

                        public void setUri(String uri) {
                            this.uri = uri;
                        }

                        public String getHeight() {
                            return height;
                        }

                        public void setHeight(String height) {
                            this.height = height;
                        }

                        public List<UrlListBeanX> getUrl_list() {
                            return url_list;
                        }

                        public void setUrl_list(List<UrlListBeanX> url_list) {
                            this.url_list = url_list;
                        }

                        public static class UrlListBeanX {
                            private String url;

                            public String getUrl() {
                                return url;
                            }

                            public void setUrl(String url) {
                                this.url = url;
                            }
                        }
                    }

                    public static class OriginVideoBean {
                        private String width;
                        private String uri;
                        private String height;
                        private List<UrlListBeanXX> url_list;

                        public String getWidth() {
                            return width;
                        }

                        public void setWidth(String width) {
                            this.width = width;
                        }

                        public String getUri() {
                            return uri;
                        }

                        public void setUri(String uri) {
                            this.uri = uri;
                        }

                        public String getHeight() {
                            return height;
                        }

                        public void setHeight(String height) {
                            this.height = height;
                        }

                        public List<UrlListBeanXX> getUrl_list() {
                            return url_list;
                        }

                        public void setUrl_list(List<UrlListBeanXX> url_list) {
                            this.url_list = url_list;
                        }

                        public static class UrlListBeanXX {
                            private String url;

                            public String getUrl() {
                                return url;
                            }

                            public void setUrl(String url) {
                                this.url = url;
                            }
                        }
                    }

                    public static class _$720pVideoBean {
                        private String width;
                        private String uri;
                        private String height;
                        private List<UrlListBeanXXX> url_list;

                        public String getWidth() {
                            return width;
                        }

                        public void setWidth(String width) {
                            this.width = width;
                        }

                        public String getUri() {
                            return uri;
                        }

                        public void setUri(String uri) {
                            this.uri = uri;
                        }

                        public String getHeight() {
                            return height;
                        }

                        public void setHeight(String height) {
                            this.height = height;
                        }

                        public List<UrlListBeanXXX> getUrl_list() {
                            return url_list;
                        }

                        public void setUrl_list(List<UrlListBeanXXX> url_list) {
                            this.url_list = url_list;
                        }

                        public static class UrlListBeanXXX {
                            private String url;

                            public String getUrl() {
                                return url;
                            }

                            public void setUrl(String url) {
                                this.url = url;
                            }
                        }
                    }

                    public static class _$480pVideoBean {
                        private String width;
                        private String uri;
                        private String height;
                        private List<UrlListBeanXXXX> url_list;

                        public String getWidth() {
                            return width;
                        }

                        public void setWidth(String width) {
                            this.width = width;
                        }

                        public String getUri() {
                            return uri;
                        }

                        public void setUri(String uri) {
                            this.uri = uri;
                        }

                        public String getHeight() {
                            return height;
                        }

                        public void setHeight(String height) {
                            this.height = height;
                        }

                        public List<UrlListBeanXXXX> getUrl_list() {
                            return url_list;
                        }

                        public void setUrl_list(List<UrlListBeanXXXX> url_list) {
                            this.url_list = url_list;
                        }

                        public static class UrlListBeanXXXX {
                            private String url;

                            public String getUrl() {
                                return url;
                            }

                            public void setUrl(String url) {
                                this.url = url;
                            }
                        }
                    }
                }

                public static class NeihanHotLinkBean {
                }

                public static class MiddleImageBean {
                    private String width;
                    private String r_height;
                    private String r_width;
                    private String uri;
                    private String height;
                    private List<UrlListBeanXXXXX> url_list;

                    public String getWidth() {
                        return width;
                    }

                    public void setWidth(String width) {
                        this.width = width;
                    }

                    public String getR_height() {
                        return r_height;
                    }

                    public void setR_height(String r_height) {
                        this.r_height = r_height;
                    }

                    public String getR_width() {
                        return r_width;
                    }

                    public void setR_width(String r_width) {
                        this.r_width = r_width;
                    }

                    public String getUri() {
                        return uri;
                    }

                    public void setUri(String uri) {
                        this.uri = uri;
                    }

                    public String getHeight() {
                        return height;
                    }

                    public void setHeight(String height) {
                        this.height = height;
                    }

                    public List<UrlListBeanXXXXX> getUrl_list() {
                        return url_list;
                    }

                    public void setUrl_list(List<UrlListBeanXXXXX> url_list) {
                        this.url_list = url_list;
                    }

                    public static class UrlListBeanXXXXX {
                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }

                public static class LargeImageListBean implements Serializable {
                    /**
                     * url : http://p1.pstatp.com/large/1e120002134b41111c57.webp
                     * url_list : [{"url":"http://p1.pstatp.com/large/1e120002134b41111c57.webp"},{"url":"http://pb3.pstatp
                     * .com/large/1e120002134b41111c57.webp"},{"url":"http://pb3.pstatp.com/large/1e120002134b41111c57.webp"}]
                     * uri : large/1e120002134b41111c57
                     * height : 560
                     * width : 417
                     * is_gif : false
                     */

                    private String url;
                    private String uri;
                    private String height;
                    private String width;
                    private boolean is_gif;
                    private List<UrlListBean> url_list;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public String getUri() {
                        return uri;
                    }

                    public void setUri(String uri) {
                        this.uri = uri;
                    }

                    public String getHeight() {
                        return height;
                    }

                    public void setHeight(String height) {
                        this.height = height;
                    }

                    public String getWidth() {
                        return width;
                    }

                    public void setWidth(String width) {
                        this.width = width;
                    }

                    public boolean isIs_gif() {
                        return is_gif;
                    }

                    public void setIs_gif(boolean is_gif) {
                        this.is_gif = is_gif;
                    }

                    public List<UrlListBean> getUrl_list() {
                        return url_list;
                    }

                    public void setUrl_list(List<UrlListBean> url_list) {
                        this.url_list = url_list;
                    }

                    public static class UrlListBean implements Serializable {
                        /**
                         * url : http://p1.pstatp.com/large/1e120002134b41111c57.webp
                         */

                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }

                public static class DislikeReasonBean {
                    private String type;
                    private String id;
                    private String title;

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }
                }

                public static class ThumbImageListBean {
                    /**
                     * url : http://p1.pstatp.com/list/s362/1e120002134b41111c57.webp
                     * url_list : [{"url":"http://p1.pstatp.com/list/s362/1e120002134b41111c57.webp"},{"url":"http://pb3.pstatp
                     * .com/list/s362/1e120002134b41111c57.webp"},{"url":"http://pb3.pstatp.com/list/s362/1e120002134b41111c57.webp"}]
                     * uri : list/s362/1e120002134b41111c57
                     * height : 362
                     * width : 362
                     * is_gif : false
                     */

                    private String url;
                    private String uri;
                    private String height;
                    private String width;
                    private boolean is_gif;
                    private List<UrlListBeanX> url_list;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public String getUri() {
                        return uri;
                    }

                    public void setUri(String uri) {
                        this.uri = uri;
                    }

                    public String getHeight() {
                        return height;
                    }

                    public void setHeight(String height) {
                        this.height = height;
                    }

                    public String getWidth() {
                        return width;
                    }

                    public void setWidth(String width) {
                        this.width = width;
                    }

                    public boolean isIs_gif() {
                        return is_gif;
                    }

                    public void setIs_gif(boolean is_gif) {
                        this.is_gif = is_gif;
                    }

                    public List<UrlListBeanX> getUrl_list() {
                        return url_list;
                    }

                    public void setUrl_list(List<UrlListBeanX> url_list) {
                        this.url_list = url_list;
                    }

                    public static class UrlListBeanX {
                        /**
                         * url : http://p1.pstatp.com/list/s362/1e120002134b41111c57.webp
                         */

                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }
            }

            public static class AdBean {
                private LogExtraBean log_extra;
                private String open_url;
                private String track_url;
                private String display_info;
                private String web_url;
                private String avatar_name;
                private long id;
                private String display_image_height;
                private String display_image_width;
                private String title;
                private String label;
                private String display_image;
                private String type;
                private String is_ad;
                private String gif_url;
                private long ad_id;
                private String button_text;
                private String display_type;
                private String click_delay;
                private String ab_show_style;
                private String avatar_url;
                private String end_time;
                private List<?> track_url_list;
                private List<FilterWordsBean> filter_words;

                public LogExtraBean getLog_extra() {
                    return log_extra;
                }

                public void setLog_extra(LogExtraBean log_extra) {
                    this.log_extra = log_extra;
                }

                public String getOpen_url() {
                    return open_url;
                }

                public void setOpen_url(String open_url) {
                    this.open_url = open_url;
                }

                public String getTrack_url() {
                    return track_url;
                }

                public void setTrack_url(String track_url) {
                    this.track_url = track_url;
                }

                public String getDisplay_info() {
                    return display_info;
                }

                public void setDisplay_info(String display_info) {
                    this.display_info = display_info;
                }

                public String getWeb_url() {
                    return web_url;
                }

                public void setWeb_url(String web_url) {
                    this.web_url = web_url;
                }

                public String getAvatar_name() {
                    return avatar_name;
                }

                public void setAvatar_name(String avatar_name) {
                    this.avatar_name = avatar_name;
                }

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public String getDisplay_image_height() {
                    return display_image_height;
                }

                public void setDisplay_image_height(String display_image_height) {
                    this.display_image_height = display_image_height;
                }

                public String getDisplay_image_width() {
                    return display_image_width;
                }

                public void setDisplay_image_width(String display_image_width) {
                    this.display_image_width = display_image_width;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }

                public String getDisplay_image() {
                    return display_image;
                }

                public void setDisplay_image(String display_image) {
                    this.display_image = display_image;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getIs_ad() {
                    return is_ad;
                }

                public void setIs_ad(String is_ad) {
                    this.is_ad = is_ad;
                }

                public String getGif_url() {
                    return gif_url;
                }

                public void setGif_url(String gif_url) {
                    this.gif_url = gif_url;
                }

                public long getAd_id() {
                    return ad_id;
                }

                public void setAd_id(long ad_id) {
                    this.ad_id = ad_id;
                }

                public String getButton_text() {
                    return button_text;
                }

                public void setButton_text(String button_text) {
                    this.button_text = button_text;
                }

                public String getDisplay_type() {
                    return display_type;
                }

                public void setDisplay_type(String display_type) {
                    this.display_type = display_type;
                }

                public String getClick_delay() {
                    return click_delay;
                }

                public void setClick_delay(String click_delay) {
                    this.click_delay = click_delay;
                }

                public String getAb_show_style() {
                    return ab_show_style;
                }

                public void setAb_show_style(String ab_show_style) {
                    this.ab_show_style = ab_show_style;
                }

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                }

                public String getEnd_time() {
                    return end_time;
                }

                public void setEnd_time(String end_time) {
                    this.end_time = end_time;
                }

                public List<?> getTrack_url_list() {
                    return track_url_list;
                }

                public void setTrack_url_list(List<?> track_url_list) {
                    this.track_url_list = track_url_list;
                }

                public List<FilterWordsBean> getFilter_words() {
                    return filter_words;
                }

                public void setFilter_words(List<FilterWordsBean> filter_words) {
                    this.filter_words = filter_words;
                }

                public static class LogExtraBean {
                    private String rit;
                    private String ad_price;
                    private String req_id;
                    private String convert_id;

                    public String getRit() {
                        return rit;
                    }

                    public void setRit(String rit) {
                        this.rit = rit;
                    }

                    public String getAd_price() {
                        return ad_price;
                    }

                    public void setAd_price(String ad_price) {
                        this.ad_price = ad_price;
                    }

                    public String getReq_id() {
                        return req_id;
                    }

                    public void setReq_id(String req_id) {
                        this.req_id = req_id;
                    }

                    public String getConvert_id() {
                        return convert_id;
                    }

                    public void setConvert_id(String convert_id) {
                        this.convert_id = convert_id;
                    }
                }

                public static class FilterWordsBean {
                    private String id;
                    private String name;
                    private boolean is_selected;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public boolean isIs_selected() {
                        return is_selected;
                    }

                    public void setIs_selected(boolean is_selected) {
                        this.is_selected = is_selected;
                    }
                }
            }

            public static class CommentsBean {
                private String text;
                private String create_time;
                private boolean user_verified;
                private String user_bury;
                private long user_id;
                private String bury_count;
                private String share_url;
                private long id;
                private String platform;
                private String is_digg;
                private String user_name;
                private String user_profile_image_url;
                private String status;
                private String description;
                private String user_digg;
                private String user_profile_url;
                private String share_type;
                private String digg_count;
                private boolean is_pro_user;
                private String platform_id;
                private String avatar_url;
                private long group_id;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public String getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(String create_time) {
                    this.create_time = create_time;
                }

                public boolean isUser_verified() {
                    return user_verified;
                }

                public void setUser_verified(boolean user_verified) {
                    this.user_verified = user_verified;
                }

                public String getUser_bury() {
                    return user_bury;
                }

                public void setUser_bury(String user_bury) {
                    this.user_bury = user_bury;
                }

                public long getUser_id() {
                    return user_id;
                }

                public void setUser_id(long user_id) {
                    this.user_id = user_id;
                }

                public String getBury_count() {
                    return bury_count;
                }

                public void setBury_count(String bury_count) {
                    this.bury_count = bury_count;
                }

                public String getShare_url() {
                    return share_url;
                }

                public void setShare_url(String share_url) {
                    this.share_url = share_url;
                }

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public String getPlatform() {
                    return platform;
                }

                public void setPlatform(String platform) {
                    this.platform = platform;
                }

                public String getIs_digg() {
                    return is_digg;
                }

                public void setIs_digg(String is_digg) {
                    this.is_digg = is_digg;
                }

                public String getUser_name() {
                    return user_name;
                }

                public void setUser_name(String user_name) {
                    this.user_name = user_name;
                }

                public String getUser_profile_image_url() {
                    return user_profile_image_url;
                }

                public void setUser_profile_image_url(String user_profile_image_url) {
                    this.user_profile_image_url = user_profile_image_url;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getUser_digg() {
                    return user_digg;
                }

                public void setUser_digg(String user_digg) {
                    this.user_digg = user_digg;
                }

                public String getUser_profile_url() {
                    return user_profile_url;
                }

                public void setUser_profile_url(String user_profile_url) {
                    this.user_profile_url = user_profile_url;
                }

                public String getShare_type() {
                    return share_type;
                }

                public void setShare_type(String share_type) {
                    this.share_type = share_type;
                }

                public String getDigg_count() {
                    return digg_count;
                }

                public void setDigg_count(String digg_count) {
                    this.digg_count = digg_count;
                }

                public boolean isIs_pro_user() {
                    return is_pro_user;
                }

                public void setIs_pro_user(boolean is_pro_user) {
                    this.is_pro_user = is_pro_user;
                }

                public String getPlatform_id() {
                    return platform_id;
                }

                public void setPlatform_id(String platform_id) {
                    this.platform_id = platform_id;
                }

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                }

                public long getGroup_id() {
                    return group_id;
                }

                public void setGroup_id(long group_id) {
                    this.group_id = group_id;
                }
            }
        }
    }
}
