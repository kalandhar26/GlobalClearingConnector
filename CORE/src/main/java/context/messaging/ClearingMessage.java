package context.messaging;

import lombok.Data;

import java.io.Serializable;

@Data
public final class ClearingMessage implements Serializable {

    private static final long serialVersion = 1L;

    private MessageHeader messageHeader;
    private MessageContent messageContent;

    public void handlePayInstructionReset(boolean resetPaymentInstruction, ClearingMessage source) {
        if(resetPaymentInstruction){
            this.getMessageHeader().setPaymentInstructionMessage((PaymentInstructionMessage)null);
        }
    }



}
