package com.tk.property.models;

public class VideosList {

	String videoid, videoname,image, video,rangeval;

	@Override
	public String toString() {
		return "VideosList [videoname=" + videoname + "]";
	}

	public String getVideoid() {
		return videoid;
	}

	public String getRangeval() {
		return rangeval;
	}

	public void setRangeval(String rangeval) {
		this.rangeval = rangeval;
	}

	public void setVideoid(String videoid) {
		this.videoid = videoid;
	}

	public String getVideoname() {
		return videoname;
	}

	public void setVideoname(String videoname) {
		this.videoname = videoname;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

}
