/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.zabeer.kafkastreaming.model;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class ItemPrice extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -7148279318899529790L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ItemPrice\",\"namespace\":\"com.zabeer.kafkastreaming.model\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"price\",\"type\":\"float\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<ItemPrice> ENCODER =
      new BinaryMessageEncoder<ItemPrice>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<ItemPrice> DECODER =
      new BinaryMessageDecoder<ItemPrice>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<ItemPrice> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<ItemPrice> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<ItemPrice> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<ItemPrice>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this ItemPrice to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a ItemPrice from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a ItemPrice instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static ItemPrice fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private java.lang.CharSequence id;
   private float price;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ItemPrice() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param price The new value for price
   */
  public ItemPrice(java.lang.CharSequence id, java.lang.Float price) {
    this.id = id;
    this.price = price;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return price;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.CharSequence)value$; break;
    case 1: price = (java.lang.Float)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public java.lang.CharSequence getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.CharSequence value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'price' field.
   * @return The value of the 'price' field.
   */
  public float getPrice() {
    return price;
  }


  /**
   * Sets the value of the 'price' field.
   * @param value the value to set.
   */
  public void setPrice(float value) {
    this.price = value;
  }

  /**
   * Creates a new ItemPrice RecordBuilder.
   * @return A new ItemPrice RecordBuilder
   */
  public static com.zabeer.kafkastreaming.model.ItemPrice.Builder newBuilder() {
    return new com.zabeer.kafkastreaming.model.ItemPrice.Builder();
  }

  /**
   * Creates a new ItemPrice RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ItemPrice RecordBuilder
   */
  public static com.zabeer.kafkastreaming.model.ItemPrice.Builder newBuilder(com.zabeer.kafkastreaming.model.ItemPrice.Builder other) {
    if (other == null) {
      return new com.zabeer.kafkastreaming.model.ItemPrice.Builder();
    } else {
      return new com.zabeer.kafkastreaming.model.ItemPrice.Builder(other);
    }
  }

  /**
   * Creates a new ItemPrice RecordBuilder by copying an existing ItemPrice instance.
   * @param other The existing instance to copy.
   * @return A new ItemPrice RecordBuilder
   */
  public static com.zabeer.kafkastreaming.model.ItemPrice.Builder newBuilder(com.zabeer.kafkastreaming.model.ItemPrice other) {
    if (other == null) {
      return new com.zabeer.kafkastreaming.model.ItemPrice.Builder();
    } else {
      return new com.zabeer.kafkastreaming.model.ItemPrice.Builder(other);
    }
  }

  /**
   * RecordBuilder for ItemPrice instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ItemPrice>
    implements org.apache.avro.data.RecordBuilder<ItemPrice> {

    private java.lang.CharSequence id;
    private float price;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.zabeer.kafkastreaming.model.ItemPrice.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.price)) {
        this.price = data().deepCopy(fields()[1].schema(), other.price);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
    }

    /**
     * Creates a Builder by copying an existing ItemPrice instance
     * @param other The existing instance to copy.
     */
    private Builder(com.zabeer.kafkastreaming.model.ItemPrice other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.price)) {
        this.price = data().deepCopy(fields()[1].schema(), other.price);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public java.lang.CharSequence getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.zabeer.kafkastreaming.model.ItemPrice.Builder setId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public com.zabeer.kafkastreaming.model.ItemPrice.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'price' field.
      * @return The value.
      */
    public float getPrice() {
      return price;
    }


    /**
      * Sets the value of the 'price' field.
      * @param value The value of 'price'.
      * @return This builder.
      */
    public com.zabeer.kafkastreaming.model.ItemPrice.Builder setPrice(float value) {
      validate(fields()[1], value);
      this.price = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'price' field has been set.
      * @return True if the 'price' field has been set, false otherwise.
      */
    public boolean hasPrice() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'price' field.
      * @return This builder.
      */
    public com.zabeer.kafkastreaming.model.ItemPrice.Builder clearPrice() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ItemPrice build() {
      try {
        ItemPrice record = new ItemPrice();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.price = fieldSetFlags()[1] ? this.price : (java.lang.Float) defaultValue(fields()[1]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<ItemPrice>
    WRITER$ = (org.apache.avro.io.DatumWriter<ItemPrice>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<ItemPrice>
    READER$ = (org.apache.avro.io.DatumReader<ItemPrice>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.id);

    out.writeFloat(this.price);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readString(this.id instanceof Utf8 ? (Utf8)this.id : null);

      this.price = in.readFloat();

    } else {
      for (int i = 0; i < 2; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readString(this.id instanceof Utf8 ? (Utf8)this.id : null);
          break;

        case 1:
          this.price = in.readFloat();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}









