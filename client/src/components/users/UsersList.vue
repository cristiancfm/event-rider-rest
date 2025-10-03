<template>
  <div class="text-start p-2">
    <h2 class="m-2" v-if="this.title" v-html="title"></h2>
    <div class="d-flex flex-wrap justify-content-start">
      <div
        class="col-sm-12 col-md-6 col-xl-4"
        v-for="user in users"
        :key="user.id"
      >
        <UserCard
          :user="user"
          :edit-users="editUsers"
          @followers="updateFollowers"
        ></UserCard>
      </div>
    </div>
    <div v-if="users.length === 0" class="text-center mt-2">
      <img
        src="../../../public/event-rider-logo-alt.png"
        style="opacity: 10%; filter: grayscale(100%)"
        alt=""
      />
      <h2>No Users</h2>
      <br />
    </div>
  </div>
</template>

<script>
import { updateFollowers } from "@/common/user";
import UserCard from "@/components/users/UserCard";

export default {
  name: "UsersList",
  props: {
    title: {
      // the title to show in the component
      type: String,
      required: true,
    },
    getUsers: {
      // the function to retrieve the users
      type: Function,
      required: true,
    },
    editUsers: {
      // whether to show edit button or not
      type: Boolean,
      required: false,
    },
  },
  data() {
    return {
      users: [],
    };
  },
  components: { UserCard },
  methods: {
    updateFollowers,
  },
  async mounted() {
    this.users = await this.getUsers();
  },
};
</script>

<style scoped></style>
