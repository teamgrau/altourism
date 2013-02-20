#pragma version(1)
#pragma rs java_package_name(com.teamgrau.altourism.rs)

void root(uchar4 *v_in) {
    uint32_t* p = (uint32_t*)v_in;
    if (*p == 0x00)
        *p = 0xff000000;
    else
        *v_in = 0x00;
}
