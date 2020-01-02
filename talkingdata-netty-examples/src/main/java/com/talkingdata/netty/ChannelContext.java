package com.talkingdata.netty;

/**
 *
 * @Date 2019/12/30 10:14
 * @Created by wj
 */
public class ChannelContext {

	private String id ;
	private String name = "ChannelContext";


	public ChannelContext(String id) {
		this.id = id;
	}

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

	@Override
	public String toString() {
		return "ChannelContext{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
