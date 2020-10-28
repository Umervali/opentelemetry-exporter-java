/*
 * Copyright 2020 New Relic Corporation. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.newrelic.telemetry.opentelemetry.export.auto;

import java.util.Properties;

class NewRelicConfiguration {
  static final String NEW_RELIC_API_KEY = "newrelic.api.key";
  static final String NEW_RELIC_ENABLE_AUDIT_LOGGING = "newrelic.enable.audit.logging";
  static final String NEW_RELIC_SERVICE_NAME = "newrelic.service.name";
  static final String DEFAULT_NEW_RELIC_SERVICE_NAME = "(unknown service)";
  static final String NEW_RELIC_TRACE_URI_OVERRIDE = "newrelic.trace.uri.override";
  static final String NEW_RELIC_METRIC_URI_OVERRIDE = "newrelic.metric.uri.override";

  // this should not be used, now that we have both span and metric exporters. Support is here
  // for any users who might still be using it.
  static final String NEW_RELIC_URI_OVERRIDE = "newrelic.uri.override";

  private final Properties config;

  NewRelicConfiguration(Properties config) {
    this.config = config;
  }

  String getApiKey() {
    return config.getProperty(NEW_RELIC_API_KEY, "");
  }

  boolean shouldEnableAuditLogging() {
    return Boolean.parseBoolean(config.getProperty(NEW_RELIC_ENABLE_AUDIT_LOGGING, "false"));
  }

  // note: newrelic.service.name key will not required once service.name is guaranteed to be
  // provided via the Resource in the SDK.  See
  // https://github.com/newrelic/opentelemetry-exporter-java/issues/62
  // for the tracking issue.
  static String getServiceName(Properties config) {
    return config.getProperty(NEW_RELIC_SERVICE_NAME, DEFAULT_NEW_RELIC_SERVICE_NAME);
  }

  String getServiceName() {
    return getServiceName(config);
  }

  boolean isMetricUriSpecified() {
    return isSpecified(getMetricUri());
  }

  String getMetricUri() {
    return config.getProperty(NEW_RELIC_METRIC_URI_OVERRIDE, "");
  }

  boolean isTraceUriSpecified() {
    return isSpecified(getTraceUri());
  }

  String getTraceUri() {
    String deprecatedUriOverride = config.getProperty(NEW_RELIC_URI_OVERRIDE, "");
    return config.getProperty(NEW_RELIC_TRACE_URI_OVERRIDE, deprecatedUriOverride);
  }

  private boolean isSpecified(String s) {
    return s != null && !s.isEmpty();
  }
}
