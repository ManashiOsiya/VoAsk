package com.voaskq.modal;

public class BroadcastList {

    String preview,resourceuri,type,created;

    public BroadcastList(String preview, String resourceuri, String type,String created) {
        this.preview = preview;
        this.resourceuri = resourceuri;
        this.type =type;
        this.created =created;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getResourceuri() {
        return resourceuri;
    }

    public void setResourceuri(String resourceuri) {
        this.resourceuri = resourceuri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
