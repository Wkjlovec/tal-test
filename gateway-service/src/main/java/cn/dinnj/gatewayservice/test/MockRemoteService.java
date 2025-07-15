package cn.dinnj.gatewayservice.test;

public class MockRemoteService {
private final long wait;

/**
 * @param wait 模拟远程调用所消耗的时间，单位 ： 毫秒
 * @throws InterruptedException
 */
public MockRemoteService(long wait) {
    this.wait = wait;
}

String execute() throws InterruptedException {
    Thread.sleep(wait);
    return "SUCCESS";
}
}
