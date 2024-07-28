package context.messaging;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MessageContent implements Serializable {

    private static final long serialVersionUID = 4626277356783537637L;

    private String inboundPayloadId;
    private String outboundPayloadId;

    private String outboundWorkflowId;
    private String messageContent;
    private String transformedMessageContent;
    private Map<String, String> messageAttributes;

    private Map<String, Object> metadataMap;

    private List<ErrorLogArgs> errorLogs;

    private String version;

    private String referenceId;




}
