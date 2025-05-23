/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.api.interception;

import org.mule.runtime.api.artifact.Registry;
import org.mule.runtime.api.component.location.ComponentLocation;

import java.util.List;
import java.util.function.Supplier;

/**
 * Abstract Factory for creating {@link SourceInterceptor} instances.
 * <p>
 * Implementations may have fields annotated with {@link jakarta.inject.Inject @Inject}, which will be resolved before attempting
 * to call {@link #get()}.
 *
 * @since 1.2
 */
public interface SourceInterceptorFactory extends Supplier<SourceInterceptor> {

  /**
   * By making a {@link SourceInterceptorOrder} available in the {@link Registry} with this key, the order in which the
   * {@link SourceInterceptorFactory SourceInterceptorFactories} products will be applied to the applicable components. can be
   * customized.
   * <p>
   * For each {@link SourceInterceptorFactory factory}, its fully qualified class name will be obtained and matched against the
   * passed {@code packagesOrder} to sort the factories. In the case there is more than one {@link SourceInterceptorFactory
   * factory} with a package name prefix, the order in which they were {@link #addInterceptorFactory(SourceInterceptorFactory)
   * added} will be kept.
   * <p>
   * Assuming this is called with parameters {@code ("org.package", "com.plugin")}, and the following
   * {@link SourceInterceptorFactory factories} have been added through {@link #addInterceptorFactory(SourceInterceptorFactory)}
   * (in this order):
   * <ol>
   * <li>{@code com.plugin.SomeInterceptor}</li>
   * <li>{@code org.mule.MuleInterceptor}</li>
   * <li>{@code org.package.logging.LoggerInterceptor}</li>
   * <li>{@code com.plugin.SomeOtherInterceptor}</li>
   * <li>{@code org.mule.OtherMuleInterceptor}</li>
   * </ol>
   * Those {@link SourceInterceptorFactory factories} will be sorted, when obtained through {@link #getInterceptorFactories()}
   * like this:
   * <ol>
   * <li>{@code org.package.logging.LoggerInterceptor}</li>
   * <li>{@code com.plugin.SomeInterceptor}</li>
   * <li>{@code com.plugin.SomeOtherInterceptor}</li>
   * <li>{@code org.mule.MuleInterceptor}</li>
   * <li>{@code org.mule.OtherMuleInterceptor}</li>
   * </ol>
   */
  public static final String SOURCE_INTERCEPTORS_ORDER_REGISTRY_KEY = "_muleSourceInterceptorFactoryOrder";

  /**
   * Determines if a {@link SourceInterceptor} shall be created by this factory to be applied to a component based on some of its
   * attributes.
   *
   * @param location the location and identification properties of the to-be intercepted component in the mule app configuration.
   * @return {@code true} if this handler must be applied to the component with the provided parameters, {@code false} otherwise.
   */
  default boolean intercept(ComponentLocation location) {
    return true;
  }

  interface SourceInterceptorOrder extends Supplier<List<String>> {

  }

}
