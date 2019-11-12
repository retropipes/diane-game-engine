package com.puttysoftware.storage;

/**
 * Data storage for objects capable of being cloned.
 */
public class CloneableObjectStorage extends ObjectStorage implements Cloneable {
  // Constructor
  /**
   * Main constructor.
   *
   * @param shape
   *          simulated dimensions for the stored data
   */
  public CloneableObjectStorage(final int... shape) {
    super(shape);
  }

  // Copy constructor
  /**
   * Main copy constructor.
   *
   * @param source
   *          the @self to make a copy of
   * @param shape
   *          simulated dimensions for the stored data
   */
  protected CloneableObjectStorage(final CloneableObject[] source,
      final int... shape) {
    super(source, shape);
  }

  // Protected copy constructor
  /**
   * Serialization-related protected copy constructor.
   *
   * @param source
   *          the underlying array where stored data came from
   */
  public CloneableObjectStorage(final CloneableObjectStorage source) {
    super(source);
  }

  /**
   * Create a copy of this @self.
   *
   * @return a copy of the current instance
   * @throws CloneNotSupportedException
   *           when copying elements fails
   */
  @Override
  protected CloneableObjectStorage clone() throws CloneNotSupportedException {
    final CloneableObjectStorage copy = new CloneableObjectStorage(
        this.getShape());
    final int rawLength = this.getRawLength();
    for (int i = 0; i < rawLength; i++) {
      final CloneableObject entryCopy = ((CloneableObject) this.getRawCell(i))
          .clone();
      copy.setRawCell(entryCopy, i);
    }
    return copy;
  }
}
