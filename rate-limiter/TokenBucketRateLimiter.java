public class TokenBucketRateLimiter {

    double tokens ;
    long lastRefillTimestamp;
    int capacity;
    private final double refillRatePerMillis;
    public TokenBucketRateLimiter(int capacity, int reflillRate){
        this.capacity = capacity;
        this.refillRatePerMillis = reflillRate / 1000.0;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }
    public synchronized boolean allowRequest() {
        refill();
        if(tokens>0){
            tokens --;
            return true;
        }else {
            return false;
        }
    }

    private void refill(){
        long now= System.currentTimeMillis();
        long timeElapsed = (now - lastRefillTimestamp);
        tokens = Math.min(capacity,tokens + timeElapsed *  refillRatePerMillis);
        lastRefillTimestamp = now;
    }

    public static void main(String[] args) {
        TokenBucketRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(10,5);
        for(int i =0 ; i<15;i++){
            Runnable task = () -> {
                boolean allowed = tokenBucketRateLimiter.allowRequest();
                System.out.println("Request allowed: " + allowed + " at " + System.currentTimeMillis());
            };
            new Thread(task).start();
        }

    }
}