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

import java.util.Map;

import org.flowable.bpmn.model.AdhocSubProcess;
import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.editor.language.json.model.ModelInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Tijs Rademakers
 */
public class AdhocSubProcessJsonConverter extends BaseBpmnJsonConverter implements FormAwareConverter, FormKeyAwareConverter,
    DecisionAwareConverter, DecisionKeyAwareConverter {

    protected Map<String, String> formMap;
    protected Map<String, ModelInfo> formKeyMap;
    protected Map<String, String> decisionMap;
    protected Map<String, ModelInfo> decisionTableKeyMap;

    public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap, Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {

        fillJsonTypes(convertersToBpmnMap);
        fillBpmnTypes(convertersToJsonMap);
    }

    public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
        convertersToBpmnMap.put(STENCIL_ADHOC_SUB_PROCESS, AdhocSubProcessJsonConverter.class);
    }

    public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
        convertersToJsonMap.put(AdhocSubProcess.class, AdhocSubProcessJsonConverter.class);
    }

    @Override
    protected String getStencilId(BaseElement baseElement) {
        return STENCIL_ADHOC_SUB_PROCESS;
    }

    @Override
    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
        AdhocSubProcess subProcess = (AdhocSubProcess) baseElement;
        propertiesNode.put("completioncondition", subProcess.getCompletionCondition());
        propertiesNode.put("ordering", subProcess.getOrdering());
        propertiesNode.put("cancelremaininginstances", subProcess.isCancelRemainingInstances());
        ArrayNode subProcessShapesArrayNode = objectMapper.createArrayNode();
        GraphicInfo graphicInfo = model.getGraphicInfo(subProcess.getId());
        processor.processFlowElements(subProcess, model, subProcessShapesArrayNode, formKeyMap,
                decisionTableKeyMap, graphicInfo.getX(), graphicInfo.getY());
        flowElementNode.set("childShapes", subProcessShapesArrayNode);
    }

    @Override
    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
        AdhocSubProcess subProcess = new AdhocSubProcess();
        subProcess.setCompletionCondition(getPropertyValueAsString("completioncondition", elementNode));
        subProcess.setOrdering(getPropertyValueAsString("ordering", elementNode));
        subProcess.setCancelRemainingInstances(getPropertyValueAsBoolean("cancelremaininginstances", elementNode));
        JsonNode childShapesArray = elementNode.get(EDITOR_CHILD_SHAPES);
        processor.processJsonElements(childShapesArray, modelNode, subProcess, shapeMap, formMap, decisionMap, model);
        return subProcess;
    }

    @Override
    public void setFormMap(Map<String, String> formMap) {
        this.formMap = formMap;
    }

    @Override
    public void setFormKeyMap(Map<String, ModelInfo> formKeyMap) {
        this.formKeyMap = formKeyMap;
    }

    @Override
    public void setDecisionMap(Map<String, String> decisionMap) {
        this.decisionMap = decisionMap;
    }

    @Override
    public void setDecisionKeyMap(Map<String, ModelInfo> decisionKeyMap) {
        this.decisionTableKeyMap = decisionKeyMap;
    }
}
