package alekseybykov.pets.tff_processor.serializer;

import alekseybykov.pets.tff_processor.models.tff.TffFile;

public interface ITffSerializer {

    String serialize(TffFile tff);
}
