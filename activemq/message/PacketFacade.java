/*    */ package org.codehaus.activemq.message;
/*    */ 
/*    */ import java.io.Externalizable;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInput;
/*    */ import java.io.ObjectOutput;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketFacade
/*    */   implements Externalizable
/*    */ {
/* 36 */   private static final WireFormat wireFormat = new DefaultWireFormat();
/*    */   
/*    */   private transient Packet packet;
/*    */ 
/*    */   
/*    */   public PacketFacade() {}
/*    */   
/*    */   public PacketFacade(Packet packet) {
/* 44 */     this.packet = packet;
/*    */   }
/*    */   
/*    */   public Packet getPacket() {
/* 48 */     return this.packet;
/*    */   }
/*    */   
/*    */   public void writeExternal(ObjectOutput out) throws IOException {
/*    */     try {
/* 53 */       wireFormat.writePacket(this.packet, out);
/*    */     }
/* 55 */     catch (JMSException e) {
/* 56 */       throw JMSExceptionHelper.newIOException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 61 */     int type = in.readByte();
/* 62 */     this.packet = wireFormat.readPacket(type, in);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\PacketFacade.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */