/*
 * Copyright 2006-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.allinfinance.dev.batch.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Entry point for browsing executions of running or historical jobs and steps.
 * Since the data may be re-hydrated from persistent storage, it may not contain
 * volatile fields that would have been present when the execution was active.
 *
 * @author Dave Syer
 * @author Michael Minella
 * @author Will Schipp
 * @author Mahmoud Ben Hassine
 * @since 2.0
 */
public interface ICustomJobExplorer extends JobExplorer {
}
