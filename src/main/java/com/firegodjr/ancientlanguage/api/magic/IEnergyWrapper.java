package com.firegodjr.ancientlanguage.api.magic;

/**
 * Wrapper Interface for unmodifiable classes
 *
 * @param <T>
 *            Type to wrap
 */
public interface IEnergyWrapper<T> {

	/**
	 * Creates an IEnergyProducer object for T
	 * 
	 * @param object
	 *            The object to produce for
	 * @return An energy producer instance for object
	 */
	public IProducerWrapper<T> createProducerFor(T object);

	public interface IProducerWrapper<T> extends IEnergyProducer {
		
		public T getRepresent();
		
	}
	
}
