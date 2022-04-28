/*     */ package org.codehaus.activemq.jndi;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.LinkRef;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameClassPair;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.NotContextException;
/*     */ import javax.naming.OperationNotSupportedException;
/*     */ import javax.naming.spi.NamingManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReadOnlyContext
/*     */   implements Context, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5754338187296859149L;
/*  51 */   protected static final NameParser nameParser = new NameParserImpl();
/*     */   
/*     */   protected final Hashtable environment;
/*     */   protected final Map bindings;
/*     */   protected final Map treeBindings;
/*     */   private boolean frozen = false;
/*     */   static final boolean $assertionsDisabled;
/*     */   
/*     */   public ReadOnlyContext() {
/*  60 */     this.environment = new Hashtable();
/*  61 */     this.bindings = new HashMap();
/*  62 */     this.treeBindings = new HashMap();
/*     */   }
/*     */   
/*     */   public ReadOnlyContext(Hashtable env) {
/*  66 */     if (env == null) {
/*  67 */       this.environment = new Hashtable();
/*     */     } else {
/*     */       
/*  70 */       this.environment = new Hashtable(env);
/*     */     } 
/*  72 */     this.bindings = Collections.EMPTY_MAP;
/*  73 */     this.treeBindings = Collections.EMPTY_MAP;
/*     */   }
/*     */   
/*     */   public ReadOnlyContext(Hashtable environment, Map bindings) {
/*  77 */     if (environment == null) {
/*  78 */       this.environment = new Hashtable();
/*     */     } else {
/*     */       
/*  81 */       this.environment = new Hashtable(environment);
/*     */     } 
/*  83 */     this.bindings = bindings;
/*  84 */     this.treeBindings = new HashMap();
/*  85 */     this.frozen = true;
/*     */   }
/*     */   
/*     */   protected ReadOnlyContext(ReadOnlyContext clone, Hashtable env) {
/*  89 */     this.bindings = clone.bindings;
/*  90 */     this.treeBindings = clone.treeBindings;
/*  91 */     this.environment = new Hashtable(env);
/*     */   }
/*     */   
/*     */   public void freeze() {
/*  95 */     this.frozen = true;
/*     */   }
/*     */   
/*     */   boolean isFrozen() {
/*  99 */     return this.frozen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map internalBind(String name, Object value) throws NamingException {
/* 116 */     assert name != null && name.length() > 0;
/* 117 */     assert !this.frozen;
/*     */     
/* 119 */     Map newBindings = new HashMap();
/* 120 */     int pos = name.indexOf('/');
/* 121 */     if (pos == -1) {
/* 122 */       if (this.treeBindings.put(name, value) != null) {
/* 123 */         throw new NamingException("Something already bound at " + name);
/*     */       }
/* 125 */       this.bindings.put(name, value);
/* 126 */       newBindings.put(name, value);
/*     */     } else {
/*     */       
/* 129 */       String segment = name.substring(0, pos);
/* 130 */       assert segment != null;
/* 131 */       assert !segment.equals("");
/* 132 */       Object o = this.treeBindings.get(segment);
/* 133 */       if (o == null) {
/* 134 */         o = newContext();
/* 135 */         this.treeBindings.put(segment, o);
/* 136 */         this.bindings.put(segment, o);
/* 137 */         newBindings.put(segment, o);
/*     */       }
/* 139 */       else if (!(o instanceof ReadOnlyContext)) {
/* 140 */         throw new NamingException("Something already bound where a subcontext should go");
/*     */       } 
/* 142 */       ReadOnlyContext readOnlyContext = (ReadOnlyContext)o;
/* 143 */       String remainder = name.substring(pos + 1);
/* 144 */       Map subBindings = readOnlyContext.internalBind(remainder, value);
/* 145 */       for (Iterator iterator = subBindings.entrySet().iterator(); iterator.hasNext(); ) {
/* 146 */         Map.Entry entry = iterator.next();
/* 147 */         String subName = segment + "/" + (String)entry.getKey();
/* 148 */         Object bound = entry.getValue();
/* 149 */         this.treeBindings.put(subName, bound);
/* 150 */         newBindings.put(subName, bound);
/*     */       } 
/*     */     } 
/* 153 */     return newBindings;
/*     */   }
/*     */   
/*     */   protected ReadOnlyContext newContext() {
/* 157 */     return new ReadOnlyContext();
/*     */   }
/*     */   
/*     */   public Object addToEnvironment(String propName, Object propVal) throws NamingException {
/* 161 */     return this.environment.put(propName, propVal);
/*     */   }
/*     */   
/*     */   public Hashtable getEnvironment() throws NamingException {
/* 165 */     return (Hashtable)this.environment.clone();
/*     */   }
/*     */   
/*     */   public Object removeFromEnvironment(String propName) throws NamingException {
/* 169 */     return this.environment.remove(propName);
/*     */   }
/*     */   
/*     */   public Object lookup(String name) throws NamingException {
/* 173 */     if (name.length() == 0) {
/* 174 */       return this;
/*     */     }
/* 176 */     Object result = this.treeBindings.get(name);
/* 177 */     if (result == null) {
/* 178 */       int pos = name.indexOf(':');
/* 179 */       if (pos > 0) {
/* 180 */         String scheme = name.substring(0, pos);
/* 181 */         Context ctx = NamingManager.getURLContext(scheme, this.environment);
/* 182 */         if (ctx == null) {
/* 183 */           throw new NamingException("scheme " + scheme + " not recognized");
/*     */         }
/* 185 */         return ctx.lookup(name);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 190 */       CompositeName path = new CompositeName(name);
/*     */       
/* 192 */       if (path.size() == 0) {
/* 193 */         return this;
/*     */       }
/*     */       
/* 196 */       Object obj = this.bindings.get(path.get(0));
/* 197 */       if (obj == null) {
/* 198 */         throw new NameNotFoundException(name);
/*     */       }
/* 200 */       if (obj instanceof Context && path.size() > 1) {
/* 201 */         Context subContext = (Context)obj;
/* 202 */         obj = subContext.lookup(path.getSuffix(1));
/*     */       } 
/* 204 */       return obj;
/*     */     } 
/*     */ 
/*     */     
/* 208 */     if (result instanceof LinkRef) {
/* 209 */       LinkRef ref = (LinkRef)result;
/* 210 */       result = lookup(ref.getLinkName());
/*     */     } 
/* 212 */     if (result instanceof javax.naming.Reference) {
/*     */       try {
/* 214 */         result = NamingManager.getObjectInstance(result, null, null, this.environment);
/*     */       }
/* 216 */       catch (NamingException e) {
/* 217 */         throw e;
/*     */       }
/* 219 */       catch (Exception e) {
/* 220 */         throw (NamingException)(new NamingException("could not look up : " + name)).initCause(e);
/*     */       } 
/*     */     }
/* 223 */     if (result instanceof ReadOnlyContext) {
/* 224 */       result = new ReadOnlyContext((ReadOnlyContext)result, this.environment);
/*     */     }
/* 226 */     return result;
/*     */   }
/*     */   
/*     */   public Object lookup(Name name) throws NamingException {
/* 230 */     return lookup(name.toString());
/*     */   }
/*     */   
/*     */   public Object lookupLink(String name) throws NamingException {
/* 234 */     return lookup(name);
/*     */   }
/*     */   
/*     */   public Name composeName(Name name, Name prefix) throws NamingException {
/* 238 */     Name result = (Name)prefix.clone();
/* 239 */     result.addAll(name);
/* 240 */     return result;
/*     */   }
/*     */   
/*     */   public String composeName(String name, String prefix) throws NamingException {
/* 244 */     CompositeName result = new CompositeName(prefix);
/* 245 */     result.addAll(new CompositeName(name));
/* 246 */     return result.toString();
/*     */   }
/*     */   
/*     */   public NamingEnumeration list(String name) throws NamingException {
/* 250 */     Object o = lookup(name);
/* 251 */     if (o == this) {
/* 252 */       return new ListEnumeration();
/*     */     }
/* 254 */     if (o instanceof Context) {
/* 255 */       return ((Context)o).list("");
/*     */     }
/*     */     
/* 258 */     throw new NotContextException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NamingEnumeration listBindings(String name) throws NamingException {
/* 263 */     Object o = lookup(name);
/* 264 */     if (o == this) {
/* 265 */       return new ListBindingEnumeration();
/*     */     }
/* 267 */     if (o instanceof Context) {
/* 268 */       return ((Context)o).listBindings("");
/*     */     }
/*     */     
/* 271 */     throw new NotContextException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object lookupLink(Name name) throws NamingException {
/* 276 */     return lookupLink(name.toString());
/*     */   }
/*     */   
/*     */   public NamingEnumeration list(Name name) throws NamingException {
/* 280 */     return list(name.toString());
/*     */   }
/*     */   
/*     */   public NamingEnumeration listBindings(Name name) throws NamingException {
/* 284 */     return listBindings(name.toString());
/*     */   }
/*     */   
/*     */   public void bind(Name name, Object obj) throws NamingException {
/* 288 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public void bind(String name, Object obj) throws NamingException {
/* 292 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws NamingException {}
/*     */ 
/*     */   
/*     */   public Context createSubcontext(Name name) throws NamingException {
/* 300 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public Context createSubcontext(String name) throws NamingException {
/* 304 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public void destroySubcontext(Name name) throws NamingException {
/* 308 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public void destroySubcontext(String name) throws NamingException {
/* 312 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public String getNameInNamespace() throws NamingException {
/* 316 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public NameParser getNameParser(Name name) throws NamingException {
/* 320 */     return nameParser;
/*     */   }
/*     */   
/*     */   public NameParser getNameParser(String name) throws NamingException {
/* 324 */     return nameParser;
/*     */   }
/*     */   
/*     */   public void rebind(Name name, Object obj) throws NamingException {
/* 328 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public void rebind(String name, Object obj) throws NamingException {
/* 332 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public void rename(Name oldName, Name newName) throws NamingException {
/* 336 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public void rename(String oldName, String newName) throws NamingException {
/* 340 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public void unbind(Name name) throws NamingException {
/* 344 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public void unbind(String name) throws NamingException {
/* 348 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   private abstract class LocalNamingEnumeration implements NamingEnumeration {
/* 352 */     private Iterator i = ReadOnlyContext.this.bindings.entrySet().iterator(); private final ReadOnlyContext this$0;
/*     */     
/*     */     public boolean hasMore() throws NamingException {
/* 355 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasMoreElements() {
/* 359 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     protected Map.Entry getNext() {
/* 363 */       return this.i.next();
/*     */     }
/*     */     public void close() throws NamingException {}
/*     */     
/*     */     private LocalNamingEnumeration() {} }
/*     */   
/*     */   private class ListEnumeration extends LocalNamingEnumeration { private final ReadOnlyContext this$0;
/*     */     
/*     */     public Object next() throws NamingException {
/* 372 */       return nextElement();
/*     */     }
/*     */     private ListEnumeration() {}
/*     */     public Object nextElement() {
/* 376 */       Map.Entry entry = getNext();
/* 377 */       return new NameClassPair((String)entry.getKey(), entry.getValue().getClass().getName());
/*     */     } }
/*     */   private class ListBindingEnumeration extends LocalNamingEnumeration { private final ReadOnlyContext this$0;
/*     */     private ListBindingEnumeration() {}
/*     */     
/*     */     public Object next() throws NamingException {
/* 383 */       return nextElement();
/*     */     }
/*     */     
/*     */     public Object nextElement() {
/* 387 */       Map.Entry entry = getNext();
/* 388 */       return new Binding((String)entry.getKey(), entry.getValue());
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\jndi\ReadOnlyContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */