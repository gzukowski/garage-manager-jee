package garagemanager.carparts.view;

import garagemanager.carparts.model.CarModel;
import garagemanager.carparts.model.PartCreateModel;
import garagemanager.carparts.service.CarService;
import garagemanager.carparts.service.PartService;
import garagemanager.component.ModelFunctionFactory;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@ConversationScoped
@Named
@Log
@NoArgsConstructor(force = true)
public class PartCreate implements Serializable {


    private PartService partService;
    private CarService carService;
    private final ModelFunctionFactory factory;

    @Getter
    private PartCreateModel part;

    @Getter
    private List<CarModel> cars;

    private final Conversation conversation;

    @Inject
    public PartCreate(
            ModelFunctionFactory factory,
            Conversation conversation
    ) {
        this.factory = factory;
        this.conversation = conversation;
    }

    @EJB
    public void setCarService(CarService carService) {
        this.carService = carService;
    }

    @EJB
    public void setPartService(PartService partService) {
        this.partService = partService;
    }

    public void init() {
        if (conversation.isTransient()) {
            cars = carService.findAll().stream()
                    .map(factory.carToModel())
                    .collect(Collectors.toList());
            part = PartCreateModel.builder()
                    .id(UUID.randomUUID())
                    .build();
            conversation.begin();
        }
    }

    public String cancelAction() {
        conversation.end();
        return "/part/part_list.xhtml?faces-redirect=true";
    }

    public String saveAction() {
        System.out.println("Part save action" + part);
        partService.createForCurrentUser(factory.modelToPart().apply(part));
        conversation.end();
        return "/part/part_list.xhtml?faces-redirect=true";
    }

    public String getConversationId() {
        return conversation.getId();
    }

}
