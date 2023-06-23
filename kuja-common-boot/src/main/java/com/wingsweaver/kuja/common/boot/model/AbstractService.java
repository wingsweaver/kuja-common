package com.wingsweaver.kuja.common.boot.model;

/**
 * 服务类的基类。<br>
 * 服务类是指提供业务逻辑的类，它们通常是无状态的，也就是说，它们不会持有任何状态，也不会修改任何状态。<br>
 * 其实现类一般被 {@link org.springframework.stereotype.Service} 注解标记。
 *
 * @author wingsweaver
 */
public abstract class AbstractService extends AbstractBusinessComponent {
}
