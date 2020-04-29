/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.dmn.api;

import java.util.List;
import java.util.Map;

import org.flowable.common.engine.api.FlowableException;

/**
 * Helper for execution a decision.
 * <p>
 * An instance can be obtained through {@link DmnDecisionService#createExecuteDecisionBuilder()}.
 * <p>
 * decisionKey should be set before calling {@link #execute()} to execute a decision.
 *
 * @author Tijs Rademakers
 */
public interface ExecuteDecisionBuilder {

    /**
     * Set the key of the decision
     **/
    ExecuteDecisionBuilder decisionKey(String decisionKey);

    /**
     * Set the parent deployment id
     */
    ExecuteDecisionBuilder parentDeploymentId(String parentDeploymentId);

    /**
     * Set the instance id
     **/
    ExecuteDecisionBuilder instanceId(String instanceId);

    /**
     * Set the execution id
     **/
    ExecuteDecisionBuilder executionId(String executionId);

    /**
     * Set the activity id
     **/
    ExecuteDecisionBuilder activityId(String activityId);

    /**
     * Set the scope type
     **/
    ExecuteDecisionBuilder scopeType(String scopeType);

    /**
     * Set the tenantId
     **/
    ExecuteDecisionBuilder tenantId(String tenantId);

    /**
     * allow to search for definition by key in the default tenant when tenant specific search fails
     */
    ExecuteDecisionBuilder fallbackToDefaultTenant();

    /**
     * Sets the variables
     */
    ExecuteDecisionBuilder variables(Map<String, Object> variables);

    /**
     * Adds a variable
     **/
    ExecuteDecisionBuilder variable(String variableName, Object value);

    /**
     * Executes a decision returning one or more output results with variables
     **/
    List<Map<String, Object>> execute();

    /**
     * Executes a decision returning one output result with variables
     **/
    Map<String, Object> executeWithSingleResult();

    /**
     * Executes a decision returning a result object including an audit trail
     **/
    DecisionExecutionAuditContainer executeWithAuditTrail();

    /**
     * Execute a decision service
     *
     * @return a Map with decision result(s) per output decision
     */
    Map<String, List<Map<String, Object>>> executeDecisionService();

    /**
     * Execute a decision service
     *
     * @return a {@link DecisionServiceExecutionAuditContainer} when a decision service was executed
     */
    DecisionServiceExecutionAuditContainer executeDecisionServiceWithAuditTrail();

    /**
     * Execute a decision service
     *
     * @return a Map with the decision service result.
     * An {@link FlowableException} will be thrown when multiple rules were hit.
     */
    Map<String, Object> executeDecisionServiceWithSingleResult();

    /**
     * Execute a single decision or a decision service depending on the provided decision key
     *
     * @return a Map with decision result(s) per decision
     */
    Map<String, List<Map<String, Object>>> evaluateDecision();

    /**
     * Execute a single decision or a decision service depending on the provided decision key
     *
     * @return the {@link DecisionExecutionAuditContainer} when a decision was executed
     * or a {@link DecisionServiceExecutionAuditContainer} when a decision service was executed
     */
    DecisionExecutionAuditContainer evaluateDecisionWithAuditTrail();

    /**
     * Execute a single decision or a decision service depending on the provided decision key
     *
     * @return a Map with the decision(s) result(s). When multiple output decisions use the same
     * variable IDs the last occurrence will be present in the Map.
     * An {@link FlowableException} will be thrown when multiple rules were hit.
     */
    Map<String, Object> evaluateDecisionWithSingleResult();

    ExecuteDecisionContext buildExecuteDecisionContext();
}
