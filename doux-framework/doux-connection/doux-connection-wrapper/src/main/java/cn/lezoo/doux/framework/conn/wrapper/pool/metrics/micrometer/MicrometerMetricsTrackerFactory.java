package cn.lezoo.doux.framework.conn.wrapper.pool.metrics.micrometer;

import cn.lezoo.doux.framework.conn.wrapper.pool.metrics.IMetricsTracker;
import cn.lezoo.doux.framework.conn.wrapper.pool.metrics.MetricsTrackerFactory;
import cn.lezoo.doux.framework.conn.wrapper.pool.metrics.PoolStats;
import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerMetricsTrackerFactory implements MetricsTrackerFactory
{

   private final MeterRegistry registry;

   public MicrometerMetricsTrackerFactory(MeterRegistry registry)
   {
      this.registry = registry;
   }

   @Override
   public IMetricsTracker create(String poolName, PoolStats poolStats)
   {
      return new MicrometerMetricsTracker(poolName, poolStats, registry);
   }
}
