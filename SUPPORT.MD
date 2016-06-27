#SKDatOBJ Currently does >NOT< support:
 - Multi-texture models (Models that have two textures applied to one object)
 - Internal references (If a model references another .DAT within the data, the referenced model will not load)
 - Any subformat other than articulated models
 - Any other-type converted to articulated (Example: rsrc/world/skybox/clockwork_skydome/widgets/base.dat will not load. It was a skybox, and thus does not have the specific data order the converter wants. I will be allowing the converter to accomidate for these changes soon)
