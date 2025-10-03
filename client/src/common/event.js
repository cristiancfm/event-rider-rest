import EventRepository from "@/repositories/EventRepository";
import UserRepository from "@/repositories/UserRepository";
import { getStore } from "@/common/store";

export async function updateSubscribers(event) {
  if (getStore().state.user.logged) {
    try {
      const account = await UserRepository.findOneBase(
        getStore().state.user.id
      );
      const index = event.subscribers.findIndex(
        (subscriber) => subscriber.id === account.id
      );
      if (index >= 0) {
        //delete subscriber
        event.subscribers.splice(index, 1);
      } else {
        //add subscriber
        event.subscribers.push(account);
      }
      await EventRepository.save(event);
    } catch (err) {
      console.error(err);
    }
  }
}

export async function updateSaves(event) {
  if (getStore().state.user.logged) {
    try {
      const account = await UserRepository.findOneBase(
        getStore().state.user.id
      );
      const index = event.saves.findIndex((save) => save.id === account.id);
      if (index >= 0) {
        //delete save
        event.saves.splice(index, 1);
      } else {
        //add save
        event.saves.push(account);
      }
      await EventRepository.save(event);
    } catch (err) {
      console.error(err);
    }
  }
}
