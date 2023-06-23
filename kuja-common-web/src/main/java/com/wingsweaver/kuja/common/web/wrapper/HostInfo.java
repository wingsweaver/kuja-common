package com.wingsweaver.kuja.common.web.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 主机信息。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class HostInfo implements Serializable {
    /**
     * 主机名称。
     */
    private String name;

    /**
     * 主机地址。
     */
    private String address;

    /**
     * 主机端口。
     */
    private int port;
}
