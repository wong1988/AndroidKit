package io.github.kit.example.bean;

import java.util.List;

public class Pet {

    /**
     * name : 真速年
     * photoUrls : ["http://dummyimage.com/160x600","http://dummyimage.com/300x600"]
     * id : 2227928488061170
     * category : {"name":"事系","id":65626094}
     * tags : [{"name":"论高厂少","id":12077184},{"name":"所非走体","id":44592166},{"name":"声段打龙","id":48391690},{"name":"县合观作","id":61240701}]
     * status : sold
     */

    private String name;
    private long id;
    /**
     * name : 事系
     * id : 65626094
     */

    private CategoryBean category;
    private String status;
    private List<String> photoUrls;
    /**
     * name : 论高厂少
     * id : 12077184
     */

    private List<TagsBean> tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class CategoryBean {
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class TagsBean {
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
