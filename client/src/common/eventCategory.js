import UserRepository from "@/repositories/UserRepository";
import { getStore } from "@/common/store";
import EventCategoryRepository from "@/repositories/EventCategoryRepository";

export async function updateSubscribers(eventCategory) {
  if (getStore().state.user.logged) {
    try {
      const account = await UserRepository.findOneBase(
        getStore().state.user.id
      );
      const index = eventCategory.subscribers.findIndex(
        (subscriber) => subscriber.id === account.id
      );
      if (index >= 0) {
        //delete subscriber
        eventCategory.subscribers.splice(index, 1);
      } else {
        //add subscriber
        eventCategory.subscribers.push(account);
      }
      await EventCategoryRepository.save(eventCategory);
    } catch (err) {
      console.error(err);
    }
  }
}
