package com;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import java.io.File;
import java.io.IOException;

public class JsonSchemaValidator {

    private static final String SCHEMA_FILE_PATH = "D:\\Personal_Repositories\\apigee-schema-validator\\apigee-json-schema-validator\\resources\\nextQuestionReqSchema.json";
    private static final String SAMPLE_RES_FILE_PATH = "D:\\Personal_Repositories\\apigee-schema-validator\\apigee-json-schema-validator\\resources\\nextQuestionReq.json";
    private static final String JSON_V4_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-04/schema#";
    private static final String JSON_SCHEMA_IDENTIFIER_ELEMENT = "$schema";

    public static void main(String[] args) {

        try{
            JsonSchemaValidator jsonSchemaValidator = new JsonSchemaValidator();
            File schemaFile = jsonSchemaValidator.readFile(SCHEMA_FILE_PATH);
            File resFile = jsonSchemaValidator.readFile(SAMPLE_RES_FILE_PATH);
            JsonNode schemaNodes = jsonSchemaValidator.getJsonNode(schemaFile);
            JsonNode resFileNodes = jsonSchemaValidator.getJsonNode(resFile);
            JsonSchema schema = jsonSchemaValidator.getJsonSchema(schemaNodes);

            boolean isSchemaValidationPassed = jsonSchemaValidator.validateSchema(schema, resFileNodes);
            if(isSchemaValidationPassed) {
                System.out.println("Schema Validation has passed!!!!");
            }else{
                System.out.println("Schema Validatio has failed!!!!");
            }

        }catch (IOException | ProcessingException e) {
            e.printStackTrace();
        }



    }

    private boolean validateSchema(JsonSchema schema, JsonNode payload) throws ProcessingException{

        ProcessingReport report = schema.validate(payload);
        return report.isSuccess();
    }

    /**
     * Method to return Json Nodes from a json file
     * @param file
     * @return
     * @throws IOException
     */
    private JsonNode getJsonNode(File file) throws IOException {
        return JsonLoader.fromFile(file);
    }

    private JsonSchema getJsonSchema(JsonNode schemaNode) throws ProcessingException {

            JsonNode schemaIdentifier = schemaNode.get(JSON_SCHEMA_IDENTIFIER_ELEMENT);
            if(null == schemaIdentifier) {
                ((ObjectNode) schemaNode).put(JSON_SCHEMA_IDENTIFIER_ELEMENT, JSON_V4_SCHEMA_IDENTIFIER);
            }

        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            return factory.getJsonSchema(schemaNode);
    }


    /**
     * Method to read schema file from a folder
     * @return
     */
    private File readFile(String filePath) {

        File schemaFile = new File(filePath);
        return schemaFile;
    }

}