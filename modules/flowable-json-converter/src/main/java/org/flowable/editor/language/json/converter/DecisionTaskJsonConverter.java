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
package org.flowable.editor.language.json.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.FieldExtension;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.ServiceTask;

import java.util.Map;

/**
 * @author Tijs Rademakers
 * @author Yvo Swillens
 */
public class DecisionTaskJsonConverter extends BaseBpmnJsonConverter implements DecisionAwareConverter {

    protected static String REFERENCE_TYPE_DECISION_TABLE = "decisionTable";
    protected static String REFERENCE_TYPE_DECISION_SERVICE = "decisionService";

    protected Map<String, String> decisionMap;

    public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap,
                                 Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {

        fillJsonTypes(convertersToBpmnMap);
        fillBpmnTypes(convertersToJsonMap);
    }

    public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
        convertersToBpmnMap.put(STENCIL_TASK_DECISION, DecisionTaskJsonConverter.class);
    }

    public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    }

    @Override
    protected String getStencilId(BaseElement baseElement) {
        return STENCIL_TASK_DECISION;
    }

    @Override
    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {

        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setType(ServiceTask.DMN_TASK);

        String decisionId = null;
        String referenceType = null;

        JsonNode decisionTableReferenceNode = getProperty(PROPERTY_DECISIONTABLE_REFERENCE, elementNode);
        if (decisionTableReferenceNode != null && decisionTableReferenceNode.has("id") && !decisionTableReferenceNode.get("id").isNull()) {
            decisionId = decisionTableReferenceNode.get("id").asText();
            referenceType = REFERENCE_TYPE_DECISION_TABLE;
        }

        JsonNode decisionServiceReferenceNode = getProperty(PROPERTY_DECISIONSERVICE_REFERENCE, elementNode);
        if (decisionServiceReferenceNode != null && decisionServiceReferenceNode.has("id") && !decisionServiceReferenceNode.get("id").isNull()) {
            decisionId = decisionServiceReferenceNode.get("id").asText();
            referenceType = REFERENCE_TYPE_DECISION_SERVICE;
        }

        if (decisionId != null && decisionMap != null) {
            String decisionTableKey = decisionMap.get(decisionId);

            FieldExtension decisionTableKeyField = new FieldExtension();
            decisionTableKeyField.setFieldName(PROPERTY_DECISIONTABLE_REFERENCE_KEY);
            decisionTableKeyField.setStringValue(decisionTableKey);
            serviceTask.getFieldExtensions().add(decisionTableKeyField);
        }

        if (referenceType != null) {
            FieldExtension decisionReferenceType = new FieldExtension();
            decisionReferenceType.setFieldName(PROPERTY_DECISION_REFERENCE_TYPE);
            decisionReferenceType.setStringValue(referenceType);
            serviceTask.getFieldExtensions().add(decisionReferenceType);
        }

        boolean decisionTableThrowErrorOnNoHitsNode = getPropertyValueAsBoolean(PROPERTY_DECISIONTABLE_THROW_ERROR_NO_HITS, elementNode);
        FieldExtension decisionTableThrowErrorOnNoHitsField = new FieldExtension();
        decisionTableThrowErrorOnNoHitsField.setFieldName(PROPERTY_DECISIONTABLE_THROW_ERROR_NO_HITS_KEY);
        decisionTableThrowErrorOnNoHitsField.setStringValue(decisionTableThrowErrorOnNoHitsNode ? "true" : "false");
        serviceTask.getFieldExtensions().add(decisionTableThrowErrorOnNoHitsField);

        boolean fallbackToDefaultTenant = getPropertyValueAsBoolean(PROPERTY_DECISIONTABLE_FALLBACK_TO_DEFAULT_TENANT, elementNode);
        FieldExtension fallbackToDefaultTenantField = new FieldExtension();
        fallbackToDefaultTenantField.setFieldName(PROPERTY_DECISIONTABLE_FALLBACK_TO_DEFAULT_TENANT_KEY);
        fallbackToDefaultTenantField.setStringValue(fallbackToDefaultTenant ? "true" : "false");
        serviceTask.getFieldExtensions().add(fallbackToDefaultTenantField);

        boolean sameDeployment = getPropertyValueAsBoolean(PROPERTY_DECISIONTABLE_SAME_DEPLOYMENT, elementNode);
        FieldExtension sameDeploymentField = new FieldExtension();
        sameDeploymentField.setFieldName(PROPERTY_DECISIONTABLE_SAME_DEPLOYMENT_KEY);
        sameDeploymentField.setStringValue(sameDeployment ? "true" : "false");
        serviceTask.getFieldExtensions().add(sameDeploymentField);

        return serviceTask;
    }

    @Override
    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {

    }

    @Override
    public void setDecisionMap(Map<String, String> decisionMap) {
        this.decisionMap = decisionMap;
    }
}
