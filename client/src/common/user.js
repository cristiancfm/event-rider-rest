import UserRepository from "@/repositories/UserRepository";
import { getStore } from "@/common/store";

export async function updateFollowers(user) {
  if (getStore().state.user.logged) {
    try {
      const account = await UserRepository.findOneBase(
        getStore().state.user.id
      );
      const index = user.followers.findIndex(
        (follower) => follower.id === account.id
      );
      if (index >= 0) {
        //delete follower
        user.followers.splice(index, 1);
      } else {
        //add follower
        user.followers.push(account);
      }
      await UserRepository.save(user);
    } catch (err) {
      console.error(err);
    }
  }
}
