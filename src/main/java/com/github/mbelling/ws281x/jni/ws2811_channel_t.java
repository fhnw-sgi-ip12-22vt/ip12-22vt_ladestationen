/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.github.mbelling.ws281x.jni;

public class ws2811_channel_t {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected ws2811_channel_t(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(ws2811_channel_t obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    @SuppressWarnings("deprecation")
    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0) {
            if (swigCMemOwn) {
                swigCMemOwn = false;
                rpi_ws281xJNI.delete_ws2811_channel_t(swigCPtr);
            }
            swigCPtr = 0;
        }
    }

    public void setGpionum(int value) {
        rpi_ws281xJNI.ws2811_channel_t_gpionum_set(swigCPtr, this, value);
    }

    public int getGpionum() {
        return rpi_ws281xJNI.ws2811_channel_t_gpionum_get(swigCPtr, this);
    }

    public void setInvert(int value) {
        rpi_ws281xJNI.ws2811_channel_t_invert_set(swigCPtr, this, value);
    }

    public int getInvert() {
        return rpi_ws281xJNI.ws2811_channel_t_invert_get(swigCPtr, this);
    }

    public void setCount(int value) {
        rpi_ws281xJNI.ws2811_channel_t_count_set(swigCPtr, this, value);
    }

    public int getCount() {
        return rpi_ws281xJNI.ws2811_channel_t_count_get(swigCPtr, this);
    }

    public void setStrip_type(int value) {
        rpi_ws281xJNI.ws2811_channel_t_strip_type_set(swigCPtr, this, value);
    }

    public int getStrip_type() {
        return rpi_ws281xJNI.ws2811_channel_t_strip_type_get(swigCPtr, this);
    }

    public void setLeds(SWIGTYPE_p_unsigned_int value) {
        rpi_ws281xJNI.ws2811_channel_t_leds_set(swigCPtr, this, SWIGTYPE_p_unsigned_int.getCPtr(value));
    }

    public SWIGTYPE_p_unsigned_int getLeds() {
        long cPtr = rpi_ws281xJNI.ws2811_channel_t_leds_get(swigCPtr, this);
        return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_int(cPtr, false);
    }

    public void setBrightness(short value) {
        rpi_ws281xJNI.ws2811_channel_t_brightness_set(swigCPtr, this, value);
    }

    public short getBrightness() {
        return rpi_ws281xJNI.ws2811_channel_t_brightness_get(swigCPtr, this);
    }

    public void setWshift(short value) {
        rpi_ws281xJNI.ws2811_channel_t_wshift_set(swigCPtr, this, value);
    }

    public short getWshift() {
        return rpi_ws281xJNI.ws2811_channel_t_wshift_get(swigCPtr, this);
    }

    public void setRshift(short value) {
        rpi_ws281xJNI.ws2811_channel_t_rshift_set(swigCPtr, this, value);
    }

    public short getRshift() {
        return rpi_ws281xJNI.ws2811_channel_t_rshift_get(swigCPtr, this);
    }

    public void setGshift(short value) {
        rpi_ws281xJNI.ws2811_channel_t_gshift_set(swigCPtr, this, value);
    }

    public short getGshift() {
        return rpi_ws281xJNI.ws2811_channel_t_gshift_get(swigCPtr, this);
    }

    public void setBshift(short value) {
        rpi_ws281xJNI.ws2811_channel_t_bshift_set(swigCPtr, this, value);
    }

    public short getBshift() {
        return rpi_ws281xJNI.ws2811_channel_t_bshift_get(swigCPtr, this);
    }

    public void setGamma(SWIGTYPE_p_unsigned_char value) {
        rpi_ws281xJNI.ws2811_channel_t_gamma_set(swigCPtr, this, SWIGTYPE_p_unsigned_char.getCPtr(value));
    }

    public SWIGTYPE_p_unsigned_char getGamma() {
        long cPtr = rpi_ws281xJNI.ws2811_channel_t_gamma_get(swigCPtr, this);
        return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_char(cPtr, false);
    }

    public ws2811_channel_t() {
        this(rpi_ws281xJNI.new_ws2811_channel_t(), true);
    }

}
